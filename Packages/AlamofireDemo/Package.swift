// swift-tools-version: 6.1
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "AlamofireDemo",
    platforms: [
      .iOS(.v13),
      .macOS(.v13),
      .tvOS(.v13),
      .watchOS(.v7),
    ],
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "AlamofireDemo",
            type: .dynamic,
            targets: ["AlamofireDemo"]),
    ],
    dependencies: [
      .package(url: "https://github.com/scade-platform/swift4j.git", from: "1.2.0"),
      .package(url: "https://github.com/Alamofire/Alamofire.git", from: "5.10.2"),
    ],
    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "AlamofireDemo",
            dependencies: [
                .product(name: "Alamofire", package: "Alamofire"),
                .product(name: "Swift4j", package: "swift4j")
        ]
        ),
        .testTarget(
            name: "AlamofireDemoTests",
            dependencies: ["AlamofireDemo"]
        ),
    ]
)
