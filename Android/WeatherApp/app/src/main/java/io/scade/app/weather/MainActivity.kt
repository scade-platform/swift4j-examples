package io.scade.app.weather


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

import io.scade.app.weather.ui.theme.WeatherAppTheme

import WeatherService.WeatherService


class MainActivity : ComponentActivity() {
    private val temperatureText = mutableStateOf("Current temperature: retrieving...")
    private lateinit var weather: WeatherService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Temperature(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // ----- Calling swift code -----

        System.loadLibrary("WeatherService")

        weather = WeatherService()

        weather.currentTemperature("Berlin") { temp, units ->
            temperatureText.value = "Current temperature in Berlin: $temp $units"
        }
    }

    @Composable
    fun Temperature(modifier: Modifier = Modifier) {
        val text by temperatureText
        Text(
            text = "$text!",
            modifier = modifier
        )
    }
}

