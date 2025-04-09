import Foundation
import GRDB
import Swift4j

@jvm
final class Database {
  private let dbWriter: any DatabaseWriter

  private init(_ dbWriter: any GRDB.DatabaseWriter) throws {
    self.dbWriter = dbWriter
    try migrator.migrate(dbWriter)
  }
      
  private var migrator: DatabaseMigrator {
    var migrator = DatabaseMigrator()

    migrator.registerMigration("v1") { db in
      try db.create(table: "player") { t in
        t.autoIncrementedPrimaryKey("id")
        t.column("name", .text).notNull()
        t.column("score", .integer).notNull()
      }
    }

    return migrator
  }

  static func create(path: String) -> Database {
    do {
      let dbQueue = try DatabaseQueue(path: path)
      return try Database(dbQueue)

    } catch {
      fatalError("Cannot open the db file: \(path). ERROR: \(error.localizedDescription)")
    }
  }

  func save(player: inout Player) {
    do {
      try dbWriter.write {
        try player.save($0)
      }
    } catch {
      fatalError(error.localizedDescription)
    }
  }

  func delete(player: Player) {
    do {
      try dbWriter.write {
        _ = try Player.deleteAll($0, keys: [player.id])
      }
    } catch {
      fatalError(error.localizedDescription)
    }
  }

  func fetch() -> [Player] {
    do {
      return try dbWriter.read {
        try Player.fetchAll($0)
      }
    } catch {
      fatalError(error.localizedDescription)
    }
  }
}


@jvm
struct Player: Codable, FetchableRecord, MutablePersistableRecord {
  var id: Int64? = nil

  var name: String
  var score: Int

  @nonjvm mutating func didInsert(_ inserted: InsertionSuccess) {
      id = inserted.rowID
  }
}
