package com.example.swiftexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.swiftexamples.ui.theme.SwiftExamplesTheme

import kotlinx.coroutines.*
import kotlinx.coroutines.future.await

import swift4j_examples.*
import swift4j_examples.viewmodel.*


class MainActivity : ComponentActivity() {

    private val observableVm: ObservableClassViewModel by viewModels {
        ObservableClassViewModelFactory(ObservableClass())
    }

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
                    Column(modifier = Modifier.padding(padding)) {
                        TestResultsScreen(results = results)
                    }
                }
            }
        }
    }

    private suspend fun runAllTests(): List<TestResult> {
        return listOf(
            runTest("Callbacks") { callbacks() },
            runTest("Arrays") { arrays() },
            runTest("Nested Classes") { nestedClasses() },
            runTest("Enums") { enums() },
            runTest("Enums with associated values") {  enumsWithAssociatedVals() },
            runTest("Vars") { vars() },
            runTest("Exceptions") {  exceptions() },
            runTest("Observable") {
                val values = observation()
                println("Observed sequence: $values")
            },
            runTest("Foundation") {  foundation() },
            runTest("Async") {  async() }
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

    fun callbacks() {
        val greetings = GreetingService()

        greetings.greet("Kotlin") {
            println("Hello, ${it.message}!")
        }
    }

    private fun arrays() {
        val arr = longArrayOf(1, 2, 3)
        println("Reverse array")
        val reversed = Arrays.reverseArray(arr)
        reversed.forEach { println(it) }

        println("Reverse back and increment by 1")
        Arrays.mapReversed(reversed) { it + 1 }.forEach { println(it) }
    }

    private fun nestedClasses() {
        val nested = ParentClass.NestedClass()
        println(nested.hello())
    }

    private fun enums() {
        println("Level: ${LevelPrinter.toString(Level.low)}")
    }

    private fun enumsWithAssociatedVals() {
        val msg = when (val res = Network.requestError()) {
            is NetworkResult.success -> "Success: "
            is NetworkResult.error -> "Error(${res.code}): ${res.message}"
            NetworkResult.loading -> "Loading"
        }

        println(msg)
    }

    private fun vars() {
        val vars = Vars(20)

        vars.x += 10

        println("X: ${vars.x}")
        println("Y: ${vars.y}")
        println("Z: ${vars.z}")

        vars.u += 10

        println("U: ${vars.u}")
        println("U: ${vars.v}")
    }

    private fun exceptions() {
        try {
            ThrowingStruct.callAndThrow()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private suspend fun observation(): List<Long> {
        val observable = ObservableClass()
        val values = mutableListOf<Long>()
        val completable = CompletableDeferred<Unit>()

        fun observeCount() {
            val count = observable.getCountWithObservationTracking {
                lifecycleScope.launch(Dispatchers.Main) {
                    observeCount()
                }
            }
            values.add(count)
            println("Observed count: $count")

            if (count == 3L) {
                completable.complete(Unit)
            }
        }

        observeCount()

        observable.count = 1
        delay(200)
        observable.count = 2
        delay(200)
        observable.count = 3

        completable.await()
        return values
    }

    private suspend fun async() {
        val async = Async()
        async.exec().await()
    }

    private fun foundation() {
        println("---------- Date -----------")
        val date = swift4j_examples.Date_example()
        println("Now is: ${date.now}")

        println("\n---------- Result ---------")
        val res = swift4j_examples.Result_example()
        val resVal = res.doWithSuccess().getOrElse { it.toString() }
        println("Result is: $resVal")
    }
}

data class TestResult(
    val name: String,
    val passed: Boolean,
    val output: String
)

@Composable
fun TestResultsScreen(results: List<TestResult>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Test Results", style = MaterialTheme.typography.headlineSmall)
        }
        items(results) { result ->
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

@Composable
fun OutputMessage(modifier: Modifier = Modifier, outputMessage: State<String>) {
    val output by outputMessage
    Text(
        text = output,
        modifier = modifier
    )
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
