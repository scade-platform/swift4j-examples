import Swift4j

@jvm
struct Vars {
  let n = 10

  var x: Int

  private var _y = 0
  var y: Int {
    get { _y }
    set { _y = newValue }
  }

  var z: Int { 0 }
  var w: Int { get { 0 } }
}


