import Swift4j

@jvm
class Arrays {
  static func mapReversed(_ arr: [Int], mapping: (Int) -> Int) -> [Int] {
    arr.reversed().map(mapping)
  }

  static func reverseArray(_ arr: [Int]) -> [Int] {
    arr.reversed()
  }
}

