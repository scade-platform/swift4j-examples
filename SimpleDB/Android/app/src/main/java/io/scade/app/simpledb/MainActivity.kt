
// MainActivity: Entry point for the SimpleDB Android app
package io.scade.app.simpledb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import SimpleDB.Database
import SimpleDB.Player



// Main activity that sets up the database and user interface
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the native library containing the Swift code and SQLite3
        System.loadLibrary("SimpleDB")

        // Get the path for the database file and create the Swift database instance
        val dbFile = getDatabasePath("players.db")
        val database = Database.create(dbFile.path)

        // Enable edge-to-edge UI and set the main Compose screen
        enableEdgeToEdge()
        setContent {
            PlayerEditorScreen(database)
        }
    }
}


@Composable
fun PlayerEditorScreen(database: Database) {
    // State for player name input
    var name by remember { mutableStateOf("") }
    // State for player score input
    var score by remember { mutableStateOf("") }
    // List of players fetched from the database
    val players = remember { mutableStateListOf(*database.fetch()) }
    // State for currently selected player
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    // Main UI layout
    Column(modifier = Modifier.padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 16.dp)) {
        // Title
        Text("Team", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        // Input for player name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Player Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Input for player score (digits only)
        OutlinedTextField(
            value = score,
            onValueChange = { score = it.filter { char -> char.isDigit() } },
            label = { Text("Score") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Row with Add and Delete buttons
        Row {
            Button(onClick = {
                // Add new player to database
                if (name.isNotBlank() && score.isNotBlank()) {
                    val newPlayer = Player(name, score.toLong())
                    database.save(newPlayer)
                    players.clear()
                    players.addAll(database.fetch())
                    name = ""
                    score = ""
                }
            }) {
                Text("Add Player")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    // Delete selected player from database
                    selectedPlayer?.let {
                        database.delete(it)
                        players.clear()
                        players.addAll(database.fetch())
                    }
                    selectedPlayer = null
                },
                enabled = selectedPlayer != null
            ) {
                Text("Delete Player")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // List of players
        LazyColumn {
            items(players) { player ->
                PlayerItem(player, isSelected = player == selectedPlayer) {
                    // Select or deselect player
                    selectedPlayer = if (selectedPlayer == player) null else player
                }
            }
        }
    }
}

// Composable for displaying a single player item
@Composable
fun PlayerItem(player: Player, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Display player name and score
            Text(text = "${player.name} - ${player.score}")
        }
    }
}
