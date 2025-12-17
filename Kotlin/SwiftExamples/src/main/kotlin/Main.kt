package org.swift.examples

import swift4j_examples.*

fun main() {
    System.loadLibrary("swift4j-examples")

    callExample("Callbacks") {  callbacks() }
    callExample("Callbacks (Async)") {  callbacksAsync() }
    callExample("Arrays") {  arrays() }
    callExample("Nested classes") {  nestedClasses() }
    callExample("Enums") {  enums() }
    callExample("Vars") {  vars() }
    callExample("Exceptions") {  exceptions() }
    callExample("Observation") {  observation() }
    callExample("Foundation") {  foundation() }
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

fun callbacksAsync() {
    val greetings = GreetingService()

    greetings.greetAsync("Kotlin", 2) {
        println(it.message)
    }

    println("Wait for a greeting...")

    Thread.sleep(2_000)

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
    print(nested.hello())
}

fun enums() {
    print("Level: ${LevelPrinter.toString(Level.low)}")
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
        println(ThrowingStruct.callAndThrow())
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
    val date = swift4j_examples.Date_example()
    println("Now is: ${date.now}")
}

