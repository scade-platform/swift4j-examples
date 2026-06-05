import Swift4j

@jvm
class ParentClass {
  #if compiler(>=6.0)
  @jvm
  #endif
  class NestedClass {
    func hello() -> String {
      return "Hello from a nested class"
    }
  }
}

