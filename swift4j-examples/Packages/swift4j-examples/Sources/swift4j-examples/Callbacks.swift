import Swift4j

@jvm
class GreetingService {
  func greet(name: String, _ response: (Greeting) -> Void) {
    let greeting = Greeting(name: name)
    response(greeting)
  }
}

@jvm
class Greeting {
  let name: String

  init(name: String) {
    self.name = name
  }

  func getMessage() -> String { "Swift runtime is greeting \(name)" }
}
