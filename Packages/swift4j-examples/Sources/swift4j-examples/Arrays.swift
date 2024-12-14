import Swift4j

@jvm class Arrays {

  static func printArray(_ arr: [Int]) {
    print("Printing array of length \(arr.count) in Swift")
    arr.forEach {
      print($0)
    }
  }

  static func mapReversed(_ arr: [Int], mapping: (Int) -> Int) -> [Int] {
    return arr.reversed().map(mapping)
  }

  static func reverseArray(_ arr: [Int]) -> [Int] {
    return arr.reversed()
  }
}
