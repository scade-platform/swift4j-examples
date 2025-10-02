// swift-tools-version: 6.0
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "SimpleDB",
    platforms: [
      .iOS(.v13),
      .macOS(.v13),
      .tvOS(.v13),
      .watchOS(.v7),
    ],
    products: [
        .library(
            name: "SimpleDB",
            type: .dynamic,
            targets: ["SimpleDB"]),
    ],
    dependencies: [
      .package(url: "https://github.com/scade-platform/swift4j.git", from: "1.3.0"),
      .package(url: "https://github.com/scade-platform/GRDB.swift.git", branch: "android_support")
      //.package(url: "https://github.com/groue/GRDB.swift.git", exact: "7.1.0")
      //.package(path: "../../../swift4j")
    ],
    targets: [
        .target(
            name: "SimpleDB",
            dependencies: [
              .product(name: "GRDB", package: "GRDB.swift"),
              .product(name: "Swift4j", package: "swift4j")
            ]
        ),
    ]
)
