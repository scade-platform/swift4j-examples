import Foundation
import GRDB
import Swift4j

/// Represents the database for storing Player records.
///
/// Handles database migrations, CRUD operations, and manages the database connection.
@jvm
final class Database {
  /// The database writer instance used for read/write operations.
  private let dbWriter: any DatabaseWriter

  /// Initializes the database with the provided writer and applies migrations.
  ///
  /// - Parameter dbWriter: The database writer instance.
  /// - Throws: An error if migration fails.
  private init(_ dbWriter: any GRDB.DatabaseWriter) throws {
    self.dbWriter = dbWriter
    // Apply database migrations
    try migrator.migrate(dbWriter)
  }

  /// The database migrator responsible for handling schema changes.
  private var migrator: DatabaseMigrator {
    var migrator = DatabaseMigrator()

    // Register the initial migration for the player table
    migrator.registerMigration("v1") { db in
      try db.create(table: "player") { t in
        t.autoIncrementedPrimaryKey("id")
        t.column("name", .text).notNull()
        t.column("score", .integer).notNull()
      }
    }

    return migrator
  }

  /// Creates and returns a Database instance for the given file path.
  ///
  /// - Parameter path: The file path to the database.
  /// - Returns: A Database instance.
  static func create(path: String) -> Database {
    do {
      // Create a database queue for the given path
      let dbQueue = try DatabaseQueue(path: path)
      return try Database(dbQueue)

    } catch {
      // Terminate if the database cannot be opened
      fatalError("Cannot open the db file: \(path). ERROR: \(error.localizedDescription)")
    }
  }

  /// Saves a player to the database. If the player is new, inserts it; otherwise, updates it.
  ///
  /// - Parameter player: The player to save (inout).
  func save(player: inout Player) {
    do {
      // Write the player to the database
      try dbWriter.write {
        try player.save($0)
      }
    } catch {
      // Terminate if saving fails
      fatalError(error.localizedDescription)
    }
  }

  /// Deletes a player from the database.
  ///
  /// - Parameter player: The player to delete.
  func delete(player: Player) {
    do {
      // Delete the player by id
      try dbWriter.write {
        _ = try Player.deleteAll($0, keys: [player.id])
      }
    } catch {
      // Terminate if deletion fails
      fatalError(error.localizedDescription)
    }
  }

  /// Fetches all players from the database.
  ///
  /// - Returns: An array of Player objects.
  func fetch() -> [Player] {
    do {
      // Read all players from the database
      return try dbWriter.read {
        try Player.fetchAll($0)
      }
    } catch {
      // Terminate if fetching fails
      fatalError(error.localizedDescription)
    }
  }
}


/// Represents a player record in the database.
///
/// Conforms to Codable, FetchableRecord, and MutablePersistableRecord for database operations.
@jvm
struct Player: Codable, FetchableRecord, MutablePersistableRecord {
  /// The unique identifier for the player (auto-incremented).
  var id: Int64? = nil

  /// The name of the player.
  var name: String
  /// The score of the player.
  var score: Int

  /// Called after the player is inserted into the database to update the id.
  ///
  /// - Parameter inserted: The insertion result containing the new row ID.
  @nonjvm mutating func didInsert(_ inserted: InsertionSuccess) {
    // Update the id with the inserted row ID
    id = inserted.rowID
  }
}
