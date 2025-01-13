package org.swift.examples

import swift4j_examples.GreetingService
import swift4j_examples.Arrays
import swift4j_examples.ParentClass

fun main() {
    System.loadLibrary("swift4j-examples")

    //callbacks()
    //arrays()
    nestedClasses()
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
    print(nested.hello())
}