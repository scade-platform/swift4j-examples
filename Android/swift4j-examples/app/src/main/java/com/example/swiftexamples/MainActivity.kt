package com.example.swiftexamples

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

import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.*

import com.example.swiftexamples.ui.theme.SwiftExamplesTheme

import swift4j_examples.GreetingService
import swift4j_examples.Arrays
import swift4j_examples.ParentClass
import swift4j_examples.Level
import swift4j_examples.LevelPrinter
import swift4j_examples.ObservableClass
import swift4j_examples.Player


class MainActivity : ComponentActivity() {
    private val outputMessage = mutableStateOf("Some text output")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwiftExamplesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OutputMessage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        System.loadLibrary("swift4j-examples")

        //callbacks()
        //arrays()
        //nestedClasses()
        //enums()
        //vars()

        observation()
    }


    @Composable
    private fun OutputMessage(modifier: Modifier = Modifier) {
        val output by outputMessage
        Text(
            text = output,
            modifier = modifier
        )
    }

    private fun callbacks() {
        val greetings = GreetingService()

        greetings.greetAsync("Kotlin", 2) {
            println(it.message)
        }

        println("Wait for a greeting...")

        Thread.sleep(5_000)

        println("Done !!!")
    }


    private fun arrays() {
        val arr = longArrayOf(1, 2, 3)

        println("Reverse array")

        val reversed = Arrays.reverseArray(arr)

        reversed.forEach {
            println(it)
        }

        println("Reverse back and increment by 1")

        Arrays.mapReversed(reversed) {
            it + 1
        }.forEach {
            println(it)
        }
    }

    private fun nestedClasses() {
        val nested = ParentClass.NestedClass()
        println(nested.hello())
    }

    private fun enums() {
        println("Level: ${LevelPrinter.toString(Level.low)}")
    }

    private fun vars() {
        val player = Player("Foo")
        println(player.name)
        player.name = "Bar"
        println(player.name)
    }

    private fun observation() {
        val observable = ObservableClass()

        suspend fun observeCount() {
            val count = withContext(Dispatchers.Main) {
                observable.getCountWithObservationTracking {
                    lifecycleScope.launch {
                        observeCount()
                    }
                }
            }
            outputMessage.value = "Count: $count"
        }

        lifecycleScope.launch {
            observeCount()
            // Postpone the update for 5 seconds
            delay(5000)
            observable.count = 1
        }
    }
}

