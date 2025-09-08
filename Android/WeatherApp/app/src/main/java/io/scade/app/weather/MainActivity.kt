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
import WeatherServiceSwiftyJson.WeatherServiceSwiftyJson
import WeatherServiceAlamofire.WeatherServiceAlamofire


class MainActivity : ComponentActivity() {
    private val temperatureText = mutableStateOf("Current temperature: retrieving...")
    private lateinit var weather: WeatherService
    // private lateinit var weather: WeatherServiceSwiftyJson
    // private lateinit var weather: WeatherServiceAlamofire

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
        // weather = WeatherServiceSwiftyJson()
        // weather = WeatherServiceAlamofire()

        weather.currentTemperature("Berlin") { temp, units ->
            temperatureText.value = "The current temperature in Berlin is: $temp $units"
        }
    }

    @Composable
    fun Temperature(modifier: Modifier = Modifier) {
        val text by temperatureText

        val parts = text.split(": ")
        val description = parts.getOrNull(0) ?: ""
        val weatherInfo = parts.getOrNull(1) ?: ""

        androidx.compose.foundation.layout.Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(text = description)
            Text(
                text = weatherInfo,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }

}

