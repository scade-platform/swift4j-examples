package com.example.alamofiredemo

import android.os.Bundle
import android.util.Log
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
import com.example.alamofiredemo.ui.theme.AlamofireDemoTheme
import AlamofireDemo.APIClient
import java.util.function.Consumer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        System.loadLibrary("AlamofireDemo") // Swift4j

        setContent {
            AlamofireDemoTheme {
                var postBody by remember { mutableStateOf("Loading...") }

                // Загружаем пост при старте
                LaunchedEffect(Unit) {
                    Log.d("MainActivity", "Launching API request...")
                    val client = APIClient()

                    // Вызываем fetchPost
                    client.fetchPost(1, Consumer { post ->
                        val text = if (post.body.isNotEmpty()) {
                            post.body
                        } else {
                            "Error: Failed to fetch post"
                        }

                        Log.d("MainActivity", "Post fetched: ${post.getSummary()}")
                        Log.d("MainActivity", "Body: $text")

                        // Обновляем Compose UI
                        postBody = text
                    })
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = postBody,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    AlamofireDemoTheme {
        Text("Sample body text")
    }
}
