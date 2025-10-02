import Observation
import Swift4j

@jvm
@Observable
public final class Counter {
  var count: Int = 0
  func increment() { count += 1 }
}
