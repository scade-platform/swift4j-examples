import Foundation
import Swift4j


@jvm
struct Date_example {
  var now: Date { .now }
}


@jvm
struct Result_example {
  func doWithSuccess() -> Result<String, Error> {
    return .success("Success!")
  }

  func doWithFailure() -> Result<String, Error> {
    return .failure(MyError.message("Failed with an error!"))
  }
}

