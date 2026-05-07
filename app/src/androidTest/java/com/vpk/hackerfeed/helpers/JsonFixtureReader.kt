package com.vpk.hackerfeed.helpers

object JsonFixtureReader {
    fun read(filename: String): String =
        javaClass.classLoader!!
            .getResourceAsStream("fixtures/$filename")!!
            .bufferedReader()
            .readText()
}
