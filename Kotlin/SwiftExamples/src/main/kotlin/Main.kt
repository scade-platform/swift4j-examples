import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import swift4j_examples.*


fun main() {
    System.loadLibrary("swift4j-examples")

    callExample("Callbacks") {  callbacks() }
    callExample("Arrays") {  arrays() }
    callExample("Nested classes") {  nestedClasses() }
    callExample("Enums") {  enums() }
    callExample("Enums with associated values") {  enumsWithAssociatedVals() }
    callExample("Vars") {  vars() }
    callExample("Exceptions") {  exceptions() }
    callExample("Observation") {  observation() }
    callExample("Foundation") {  foundation() }
    callExample("Async") {  async() }
}

fun callExample(name: String, example: () -> Unit) {
    val nameLabel = "========== $name =========="
    val sep = "=".repeat(nameLabel.length)

    println(nameLabel)
    println(sep)

    example()

    println(sep)
    println()
}

fun callbacks() {
    val greetings = GreetingService()

    greetings.greet("Kotlin") {
        println("Hello, ${it.message}!")
    }
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
    print(nested.hello())
}

fun enums() {
    print("Level: ${LevelPrinter.toString(Level.low)}")
}

fun enumsWithAssociatedVals() {
    val msg = when (val res = Network.requestError()) {
        is NetworkResult.success -> "Success: "
        is NetworkResult.error -> "Error(${res.code}): ${res.message}"
        NetworkResult.loading -> "Loading"
    }

    println(msg)
}

fun vars() {
    val vars = Vars(20)

    vars.x += 10

    println("X: ${vars.x}")
    println("Y: ${vars.y}")
    println("Z: ${vars.z}")

    vars.u += 10

    println("U: ${vars.u}")
    println("U: ${vars.v}")
}

fun exceptions() {
    try {
        ThrowingStruct.callAndThrow()
    } catch (e: Exception) {
        println(e.message)
    }
}

fun observation() {
    val observable = ObservableClass()

    fun onCountChange() {
        println("Count is about to be changed")
        observable.getCountWithObservationTracking {
            onCountChange()
        }
    }

    val curCount = observable.getCountWithObservationTracking {
        onCountChange()
    }

    println("Count is $curCount")
    observable.count = 1
    println("Count is ${observable.count}")

    fun onTitleChange() {
        println("Titile is about to be changed")
        observable.getTitleWithObservationTracking {
            onTitleChange()
        }
    }

    val curTitle = observable.getTitleWithObservationTracking {
        onTitleChange()
    }
    println("Title is: $curTitle")
    observable.count = 2
    println("Title is: ${observable.title}")
}

fun foundation() {
    println("---------- Date -----------")
    val date = swift4j_examples.Date_example()
    println("Now is: ${date.now}")

    println("\n---------- Result ---------")
    val res = swift4j_examples.Result_example()
    val resVal = res.doWithSuccess().getOrElse { it.toString() }
    println("Result is: $resVal")
}


fun async() {
    val async = Async()

    runBlocking {
        async.exec().await()
    }
}
