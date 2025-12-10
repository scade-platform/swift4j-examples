import XCTest
@testable import SalesforceDemo

final class SalesforceDemoTests: XCTestCase {

    func testLoadAccounts() async throws {
        let api = SalesforceAPI()
        
        let accounts = try await api.loadAccounts()
        
        print("✅ Получено аккаунтов: \(accounts.count)")
        
        for acc in accounts {
            print("- \(acc.name)")
        }
        
        XCTAssertFalse(accounts.isEmpty)
    }
}
