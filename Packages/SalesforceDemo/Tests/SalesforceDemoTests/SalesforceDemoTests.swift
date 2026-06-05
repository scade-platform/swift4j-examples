import XCTest
@testable import SalesforceDemo

final class SalesforceDemoTests: XCTestCase {

    func testLoadAccounts() async throws {
        let api = SalesforceAPI(
            clientId:     TestCredentials.clientId,
            clientSecret: TestCredentials.clientSecret,
            baseDomain:   TestCredentials.baseDomain
        )
        
        let accounts = try await api.loadAccounts()
        
        print("✅ Accounts received: \(accounts.count)")
        
        for acc in accounts {
            print("- \(acc.name)")
        }
        
        XCTAssertFalse(accounts.isEmpty)
    }
}
