package com.example.observation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier

import com.example.observation.ui.theme.ObservationDemoTheme

import ObservationDemo.Counter
import ObservationDemo.viewmodel.CounterViewModel
import ObservationDemo.viewmodel.CounterViewModelFactory


class MainActivity : ComponentActivity() {
    private val counterVm: CounterViewModel by viewModels {
        CounterViewModelFactory(Counter())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        System.loadLibrary("ObservationDemo")

        setContent {
            ObservationDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CounterScreen(counterVm, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CounterScreen(viewModel: CounterViewModel,
                  modifier: Modifier = Modifier) {

    val count by viewModel.count.collectAsState()

    Column(modifier) {
        Text("Count: $count")
        Button(onClick = { viewModel.updateCount(count + 1) }) {
            Text("Increment")
        }
    }
}