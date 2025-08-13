// swift-tools-version:6.1
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
        .library(
            name: "AlamofireDemo",
            type: .dynamic,
            targets: ["AlamofireDemo"]
        ),
    ],
    dependencies: [
        .package(url: "https://github.com/scade-platform/swift4j.git", from: "1.2.0"),
        .package(url: "https://github.com/Alamofire/Alamofire.git", from: "5.10.2"),
    ],
    targets: [
        .target(
            name: "AlamofireDemo",
            dependencies: [
                .product(name: "Alamofire", package: "Alamofire"),
                .product(name: "Swift4j", package: "swift4j"),
            ]
        ),
        .executableTarget(
            name: "AlamofireDemoRunner",
            dependencies: [
                .target(name: "AlamofireDemo")
            ]
        ),
        .testTarget(
            name: "AlamofireDemoTests",
            dependencies: ["AlamofireDemo"]
        ),
    ]
)
