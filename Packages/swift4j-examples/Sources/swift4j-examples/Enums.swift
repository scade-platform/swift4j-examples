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


