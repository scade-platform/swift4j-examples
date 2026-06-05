import Swift4j


@jvm
struct Opts {
  func passOpt(_ opt: Int?) {}

  func passOptCallback(_ callback: (() -> Void)? = nil) {
    callback?()
  }

  func passOptArray(_ arr: [Int]?) {
    
  }
}
