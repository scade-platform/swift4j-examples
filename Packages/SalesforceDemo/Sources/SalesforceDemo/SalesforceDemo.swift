import Foundation
import Swift4j
import Dispatch
import os

// MARK: - Models

public struct TokenResponse: Codable {
    public let access_token: String
}

public struct Account: Identifiable, Codable {
    public let id: String
    public let name: String
    public let accountNumber: String?
    public let type: String?
    
    enum CodingKeys: String, CodingKey {
        case id = "Id"
        case name = "Name"
        case accountNumber = "AccountNumber"
        case type = "Type"
    }
}

public struct AccountsQueryResponse: Codable {
    public let totalSize: Int
    public let done: Bool
    public let records: [Account]
}

// MARK: - Internal HTTP result holder

final class RequestResult: @unchecked Sendable {
    var data: Data?
    var response: URLResponse?
    var error: Error?
}

// MARK: - Salesforce API Client (pure Swift, no @jvm)

public final class SalesforceAPI {
    
    private let clientId     = "client_id"
    private let clientSecret = "client_secret"
    
    private let baseDomain   = "orgfarm-c4ae2aaf32-dev-ed.develop.my.salesforce.com"
    private let apiVersion   = "v65.0"
    
    public init() {}
    
    public func loadAccounts() throws -> [Account] {
        let token = try fetchAccessToken()
        let response = try fetchAccounts(accessToken: token)
        return response.records
    }
    
    // MARK: - HTTP Helper
    
    private func performRequest(_ request: URLRequest) throws -> (Data, URLResponse) {
        let semaphore = DispatchSemaphore(value: 0)
        let result = RequestResult()
        
        os_log("performRequest start");

        URLSession.shared.dataTask(with: request) { data, response, error in
            result.data = data
            result.response = response
            result.error = error
            semaphore.signal()
        }.resume()
        
        semaphore.wait()
        
        if let error = result.error {
            os_log("performRequest request completed with error: \(error)");
            throw error
        }

        os_log("performRequest request completed");

        guard let data = result.data, let response = result.response else {
            os_log("performRequest completed with no data");
            throw URLError(.badServerResponse)
        }

        os_log("performRequest received data: \(data)");
        
        return (data, response)
    }
    
    // MARK: - Token
    
    private func fetchAccessToken() throws -> String {
        var components = URLComponents()
        components.scheme = "https"
        components.host = baseDomain
        components.path = "/services/oauth2/token"
        
        guard let url = components.url else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        
        var body = URLComponents()
        body.queryItems = [
            .init(name: "grant_type", value: "client_credentials"),
            .init(name: "client_id", value: clientId),
            .init(name: "client_secret", value: clientSecret)
        ]

        request.httpBody = body.percentEncodedQuery?.data(using: .utf8)
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")

        let (data, response) = try performRequest(request)
        let http = response as! HTTPURLResponse
        
        guard 200..<300 ~= http.statusCode else {
            let text = String(data: data, encoding: .utf8) ?? ""
            throw NSError(
                domain: "TokenError",
                code: http.statusCode,
                userInfo: [NSLocalizedDescriptionKey: text]
            )
        }
        
        return try JSONDecoder().decode(TokenResponse.self, from: data).access_token
    }
    
    // MARK: - Accounts
    
    private func fetchAccounts(accessToken: String) throws -> AccountsQueryResponse {
        var components = URLComponents()
        components.scheme = "https"
        components.host = baseDomain
        components.path = "/services/data/\(apiVersion)/query"
        components.queryItems = [
            .init(name: "q", value: "SELECT Id, Name, AccountNumber, Type FROM Account")
        ]
        
        guard let url = components.url else {
            throw URLError(.badURL)
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.setValue("Bearer \(accessToken)", forHTTPHeaderField: "Authorization")
        
        let (data, response) = try performRequest(request)
        let http = response as! HTTPURLResponse
        
        guard 200..<300 ~= http.statusCode else {
            let text = String(data: data, encoding: .utf8) ?? ""
            throw NSError(
                domain: "AccountsError",
                code: http.statusCode,
                userInfo: [NSLocalizedDescriptionKey: text]
            )
        }
        
        return try JSONDecoder().decode(AccountsQueryResponse.self, from: data)
    }
}

// MARK: - JVM Bridge

@jvm
public final class SalesforceBridge {
    
    public init() {}
    
    public func loadAccountsJson() -> String {
        let api = SalesforceAPI()
        os_log("loadAccountsJson begin")
        
        do {
            let accounts = try api.loadAccounts()
            os_log("loadAccountsJson accounts: \(accounts.count)")
            let data = try JSONEncoder().encode(accounts)
            return String(data: data, encoding: .utf8) ?? "[]"
        } catch {
            os_log("ERROR :\(error)")
            return "[]"
        }
    }
}
