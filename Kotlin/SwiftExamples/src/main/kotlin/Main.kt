package org.swift.examples

import swift4j_examples.GreetingService
import swift4j_examples.Arrays
import swift4j_examples.ParentClass
import swift4j_examples.WeatherService
import swift4j_examples.Level
import swift4j_examples.LevelPrinter
import swift4j_examples.Player

fun main() {
    System.loadLibrary("swift4j-examples")

    //callbacks()
    //arrays()
    //nestedClasses()
    //weather()
    //enums()
    vars()
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

fun weather() {
    val weather = WeatherService()

    weather.currentTemperature(53.86972F, 10.686389F) { temp, units ->
        println("Current temperature: $temp $units")
    }

    Thread.sleep(5_000)
}

fun enums() {
    print("Level: ${LevelPrinter.toString(Level.low)}")
}

fun vars() {
    val player = Player("Foo")
    println(player.name)
    player.name = "Bar"
    println(player.name)
}