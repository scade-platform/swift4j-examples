import Swift4j

@jvm
struct Player {
  var name: String
}

@jvm
struct Team {
  func save(player: inout Player) { }
}
