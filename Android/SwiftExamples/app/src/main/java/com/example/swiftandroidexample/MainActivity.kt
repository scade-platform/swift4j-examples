package com.example.swiftandroidexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.swiftandroidexample.ui.theme.SwiftAndroidExampleTheme

import swift4j_examples.WeatherService

class MainActivity : ComponentActivity() {
    private val temperatureText = mutableStateOf("Current temperature: retrieving...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SwiftAndroidExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Temperature(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // ----- Calling swift-examples demo -----

        System.loadLibrary("swift4j-examples")

        val weather = WeatherService()
        weather.currentTemperature(53.86972F, 10.686389F) { temp, units ->
            temperatureText.value = "Current temperature in Lübeck: $temp $units"
        }

        // ----------------------------------------

    }

    @Composable
    fun Temperature(modifier: Modifier = Modifier) {
        val text by temperatureText
        Text(
            text = "$text!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        SwiftAndroidExampleTheme {
            Temperature()
        }
    }
}

