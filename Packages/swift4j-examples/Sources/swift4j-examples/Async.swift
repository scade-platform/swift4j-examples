import Swift4j
import Dispatch

@jvm
struct Async {
  func exec() async throws {
    for i in 0..<10 {
      print("Step \(i)")
      try await Task.sleep(nanoseconds: UInt64(1e9))
    }
  }

  // Do not use overloading here as Kotlin cannot resolve it
  func execWithResult() async -> String {
    guard let _ = try? await Task.sleep(nanoseconds: UInt64(2e9)) else {
      return "Failed to wait"
    }

    return "Swift greets Kotlin from an async function"
  }
}
