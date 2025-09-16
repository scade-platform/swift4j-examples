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
            targets: [
              "WeatherService",
              "WeatherServiceSwiftyJson",
              "WeatherServiceAlamofire"
            ])
    ],

    dependencies: [      
      .package(url: "https://github.com/scade-platform/swift4j.git", from: "1.2.1"),
      .package(url: "https://github.com/scade-platform/SwiftyJSON.git", revision: "3594d05"),
      .package(url: "https://github.com/Alamofire/Alamofire.git", from: "5.10.2")
      //.package(path: "../../../swift4j")
    ],

    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "WeatherService",
            dependencies: [
              .product(name: "Swift4j", package: "swift4j")
            ]
        ),
        .target(
            name: "WeatherServiceSwiftyJson",
            dependencies: [
              .product(name: "Swift4j", package: "swift4j"),
              .product(name: "SwiftyJSON", package: "SwiftyJSON")
            ]
        ),
        .target(
            name: "WeatherServiceAlamofire",
            dependencies: [
              .product(name: "Swift4j", package: "swift4j"),
              .product(name: "SwiftyJSON", package: "SwiftyJSON"),
              .product(name: "Alamofire", package: "Alamofire")
            ]
        )
    ]
)

