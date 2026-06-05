import Swift4j

@jvm
struct Vars {
  let n = 10

  var x: Int

  var y: Int { 0 }

  var z: Int { get { 0 } }

  private var _u = 0
  var u: Int {
    get { _u }
    set { _u = newValue }
  }

  private(set) var v: Int {
    get { _u }
    set { _u = newValue }
  }
}


