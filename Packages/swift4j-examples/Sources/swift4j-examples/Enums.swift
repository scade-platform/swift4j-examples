import Swift4j
import Foundation

@jvm
enum Level {
  case low
  case medium
  case high
}


@jvm
class LevelPrinter {
  static func toString(lvl: Level) -> String {
    switch lvl {
      case .low: return "low"
      case .medium: return "medium"
      case .high: return "high"
    }
  }
}


@jvm
enum NetworkResult {
  case success(String)
  case error(code: Int, message: String)
  case loading
}



@jvm
struct Network {
  static func requestSuccess() -> NetworkResult {
    return .success("success")
  }

  static func requestError() -> NetworkResult {
    return .error(code: 404, message: "Not found")
  }

  static func requestLoading() -> NetworkResult {
    return .loading
  }


  static func forward(_ result: NetworkResult) -> NetworkResult {
    switch result {
      case .success(let s): return .success("Forwarded: \(s)")
      case .error(let i, let s): return .error(code: -i, message: "Forwarded: \(s)")
      case .loading: return .loading
    }
  }
}
