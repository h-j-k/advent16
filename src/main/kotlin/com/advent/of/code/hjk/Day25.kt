package com.advent.of.code.hjk

object Day25 {

    fun part1(input: List<String>): Int {
        val target = listOf(1, 2).map { input[it].split(" ")[1].toInt() }.let { (a, b) -> a * b }
        return generateSequence(1) { answer ->
            (answer + 1).takeUnless { (target + it - 1).toString(2).matches("(10)+".toRegex()) }
        }.last()
    }
}