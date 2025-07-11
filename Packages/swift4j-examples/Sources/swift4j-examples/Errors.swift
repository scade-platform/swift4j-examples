import Swift4j
import Foundation

enum MyError: LocalizedError {
  case message(String)

  var errorDescription: String? {
    switch self {
    case .message(let msg):
      return msg
    }
  }
}

@jvm
struct ThrowingStruct {
  static func callAndThrow(_ arg: Int) throws {
    throw MyError.message("An error occurred!")
  }

  static func callAndThrow() throws{
    throw MyError.message("An error occurred!")
  }
}
