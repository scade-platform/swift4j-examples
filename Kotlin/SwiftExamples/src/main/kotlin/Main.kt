package org.swift.examples

import swift4j_examples.*

fun main() {
    System.loadLibrary("swift4j-examples")

    //callbacks()
    //callbacks_async()
    //arrays()
    //nestedClasses()
    //enums()
    //vars()
    //exceptions()
    //observation()
    //foundation()
    reducer()
}

fun callbacks() {
    val greetings = GreetingService()

    val greeting = greetings.greet("Kotlin") {
        "Hello, ${it.message}!"
    }

    println(greeting)
}

fun callbacks_async() {
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
    print(nested.hello())
}

fun enums() {
    print("Level: ${LevelPrinter.toString(Level.low)}")
}

fun vars() {
    val vars = Vars(20)

    vars.x += 10
    println("X: ${vars.x}")

    vars.y += 10
    println("Y: ${vars.y}")

    println("Z: ${vars.z}")
    println("W: ${vars.w}")
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
}

fun foundation() {
    val date = swift4j_examples.Date_example()
    println("Now is: ${date.now}")
}

