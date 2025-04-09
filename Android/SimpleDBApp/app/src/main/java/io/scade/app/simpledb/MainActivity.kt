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


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.loadLibrary("SimpleDB")

        val dbFile = getDatabasePath("players.db")
        val database = Database.create(dbFile.path)

        enableEdgeToEdge()
        setContent {
            PlayerEditorScreen(database)
        }
    }
}


@Composable
fun PlayerEditorScreen(database: Database) {
    var name by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }
    val players = remember { mutableStateListOf(*database.fetch()) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    Column(modifier = Modifier.padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 16.dp)) {
        Text("Team", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Player Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = score,
            onValueChange = { score = it.filter { char -> char.isDigit() } },
            label = { Text("Score") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = {
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
            Button(onClick = {
                selectedPlayer?.let {
                    database.delete(it)
                    players.clear()
                    players.addAll(database.fetch())
                }
                selectedPlayer = null
            }, enabled = selectedPlayer != null) {
                Text("Delete Player")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(players) { player ->
                PlayerItem(player, isSelected = player == selectedPlayer) {
                    selectedPlayer = if (selectedPlayer == player) null else player
                }
            }
        }
    }
}

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
            Text(text = "${player.name} - ${player.score}")
        }
    }
}
