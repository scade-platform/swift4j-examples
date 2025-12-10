package com.example.salesforcedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.salesforcedemo.ui.theme.SalesforceDemoTheme
import SalesforceDemo.SalesforceBridge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SalesforceDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AccountsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AccountsScreen(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        text = loadFromSwift()
    }

    Text(
        text = text,
        modifier = modifier
    )
}

suspend fun loadFromSwift(): String {
    return try {
        val bridge = SalesforceBridge()
        bridge.loadAccountsJson()
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SalesforceDemoTheme {
        AccountsScreen()
    }
}
