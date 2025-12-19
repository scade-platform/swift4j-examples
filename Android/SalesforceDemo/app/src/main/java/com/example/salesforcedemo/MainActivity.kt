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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("SalesforceDemo")

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
    var text by remember { mutableStateOf("Loading..." ) }

    LaunchedEffect(Unit) {
        try {
            val bridge = SalesforceBridge()
            val result = bridge.loadAccounts() { accounts ->
                text = accounts.joinToString("\n") { it.name }
            }
        } catch (e: Exception) {
            text = "Error: ${e.message}"
        }
    }

    Text(
        text = text,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AccountsScreenPreview() {
    SalesforceDemoTheme {
        AccountsScreen()
    }
}
