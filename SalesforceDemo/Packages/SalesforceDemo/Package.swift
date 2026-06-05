// swift-tools-version: 6.2
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "SalesforceDemo",
    platforms: [
        .macOS(.v12),
        .iOS(.v18)
    ],
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "SalesforceDemo",
            type: .dynamic,
            targets: ["SalesforceDemo"]
        ),
    ],
    dependencies: [
            .package(url: "https://github.com/scade-platform/swift4j.git", branch: "main")
        ],
    targets: [
        .target(
            name: "SalesforceDemo",
            dependencies: [
                .product(name: "Swift4j", package: "swift4j")
            ]
        ),
        .executableTarget(
            name: "GenerateTestCredentialsTool"
        ),
        .plugin(
            name: "GenerateTestCredentials",
            capability: .buildTool(),
            dependencies: ["GenerateTestCredentialsTool"]
        ),
        .testTarget(
            name: "SalesforceDemoTests",
            dependencies: ["SalesforceDemo"],
            plugins: [.plugin(name: "GenerateTestCredentials")]
        ),
    ]
)
