import Foundation
import Swift4j
import Dispatch
import os

// MARK: - Models

public struct TokenResponse: Codable {
    public let access_token: String
}

@jvm
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
    
    public func loadAccounts() async throws -> [Account] {
        let token = try await fetchAccessToken()
        let response = try await fetchAccounts(accessToken: token)
        return response.records
    }
    
    // MARK: - HTTP Helper
    
    private func performRequest(_ request: URLRequest) async throws -> Data {
        os_log("performRequest start");

        let (data, response) = try await URLSession.shared.data(for: request);

        os_log("performRequest request completed");

        guard let httpResponse = response as? HTTPURLResponse else {
            os_log("performRequest response is not a HTTPURLResponse");

            throw NSError(
                domain: "AccountsError",
                code: -1,
                userInfo: [NSLocalizedDescriptionKey: "response is not a HTTPURLResponse"]
            )
        }

        guard 200..<300 ~= httpResponse.statusCode else {
            let text = String(data: data, encoding: .utf8) ?? ""
            os_log("performRequest received bad status code: \(httpResponse.statusCode), error: \(text)");

            throw NSError(
                domain: "AccountsError",
                code: httpResponse.statusCode,
                userInfo: [NSLocalizedDescriptionKey: text]
            )
        }

        os_log("performRequest finished");
        
        return data
    }
    
    // MARK: - Token
    
    private func fetchAccessToken() async throws -> String {
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

        let data = try await performRequest(request)
        return try JSONDecoder().decode(TokenResponse.self, from: data).access_token
    }
    
    // MARK: - Accounts
    
    private func fetchAccounts(accessToken: String) async throws -> AccountsQueryResponse {
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
        
        let data = try await performRequest(request)
        return try JSONDecoder().decode(AccountsQueryResponse.self, from: data)
    }
}

// MARK: - JVM Bridge

@jvm
public final class SalesforceBridge {
    let api = SalesforceAPI()
    
    public init() {}

    public func loadAccounts(callback: ([Account]) -> Void) async {
        do {
            os_log("loadAccounts started")
            let accounts = try await api.loadAccounts()
            os_log("loadAccounts received accounts: \(accounts)")
            callback(accounts)
            os_log("loadAccounts completed")
        }
        catch {
            os_log("loadAccounts error: \(error)")
            callback([])
        }
    }
}
