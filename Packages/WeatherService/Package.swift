// swift-tools-version: 5.10
// The swift-tools-version declares the minimum version of Swift required to build this package.

import Foundation
import PackageDescription

let package = Package(
    name: "WeatherService",
    platforms: [.macOS(.v13)],

    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "WeatherService",
            type: .dynamic,
            targets: ["WeatherService"])
    ],

    dependencies: [
      //.package(url: "https://github.com/scade-platform/swift4j.git", branch: "main")
      //.package(url: "https://github.com/scade-platform/swift4j.git", from: "1.0.0")
      .package(path: "../../../swift4j")
    ],

    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "WeatherService",
            dependencies: [
              .product(name: "Swift4j", package: "swift4j")
            ]
        )
    ]
)

