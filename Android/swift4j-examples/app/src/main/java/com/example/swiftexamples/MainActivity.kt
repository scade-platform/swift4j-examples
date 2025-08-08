package com.example.swiftexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.swiftexamples.ui.theme.SwiftExamplesTheme
import swift4j_examples.GreetingService
import swift4j_examples.Arrays
import swift4j_examples.ParentClass

import swift4j_examples.Level
import swift4j_examples.LevelPrinter
import swift4j_examples.Player
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        System.loadLibrary("swift4j-examples")

        setContent {
            SwiftExamplesTheme {
                var results by remember { mutableStateOf<List<TestResult>>(emptyList()) }

                LaunchedEffect(Unit) {
                    results = runAllTests()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    TestResultsScreen(results = results, modifier = Modifier.padding(padding))
                }
            }
        }
    }

    private suspend fun runAllTests(): List<TestResult> {
        return listOf(
            runTest("Callbacks") { callbacks() },
            //runTest("Arrays") { arrays() },
            runTest("Nested Classes") { nestedClasses() },
            runTest("Enums") { enums() },
            runTest("Vars") { vars() }
        )
    }

    private suspend fun runTest(name: String, test: suspend () -> Unit): TestResult {
        val originalOut = System.out
        val outputStream = java.io.ByteArrayOutputStream()
        val printStream = java.io.PrintStream(outputStream)
        System.setOut(printStream)

        var passed = true

        try {
            test()
        } catch (e: Exception) {
            e.printStackTrace()
            passed = false
        }

        System.out.flush()
        System.setOut(originalOut)

        val output = outputStream.toString()

        return TestResult(name, passed, output)
    }

}

data class TestResult(
    val name: String,
    val passed: Boolean,
    val output: String
)


@Composable
fun TestResultsScreen(results: List<TestResult>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Test Results", style = MaterialTheme.typography.headlineSmall)
        for (result in results) {
            TestResultRow(result)
        }
    }
}


@Composable
fun TestResultRow(result: TestResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                if (result.passed) "✅" else "❌",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "${result.name}: ${if (result.passed) "Test Passed" else "Test Failed"}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (result.output.isNotBlank()) {
            Text(
                result.output,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 24.dp, top = 4.dp)
            )
        }
    }
}

fun callbacks() {
    val greetings = GreetingService()

    greetings.greetAsync("Kotlin", 2) {
        println(it.message)
    }

    println("Wait for a greeting...")

    Thread.sleep(5_000)

    println("Done !!!")
}


fun arrays() {
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

fun nestedClasses() {
    val nested = ParentClass.NestedClass()
    println(nested.hello())
}

fun enums() {
    println("Level: ${LevelPrinter.toString(Level.low)}")
}

fun vars() {
    val player = Player("Player1")
    println(player.name)
    player.name = "Player2"
    println(player.name)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SwiftExamplesTheme {
        Greeting("Android")
    }
}