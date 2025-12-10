// swift-tools-version: 6.0
// The swift-tools-version declares the minimum version of Swift required to build this package.

import Foundation
import PackageDescription

let package = Package(
    name: "swift4j-examples",
    platforms: [.macOS(.v14)],

    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "swift4j-examples",
            type: .dynamic,
            targets: ["swift4j-examples"])
    ],

    dependencies: [
      .package(url: "https://github.com/scade-platform/swift4j.git", from: "1.3.1")
    ],

    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "swift4j-examples",
            dependencies: [
              .product(name: "Swift4j", package: "swift4j")
            ]
        )
    ]
)

