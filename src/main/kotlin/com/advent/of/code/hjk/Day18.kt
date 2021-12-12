package com.advent.of.code.hjk

object Day18 {

    fun part1(input: String, times: Int): Int = generateSequence(input) { previous ->
        input.indices.joinToString("") { i ->
            when (((i - 1)..(i + 1)).joinToString("") { if (it in input.indices) "${previous[it]}" else "." }) {
                "^^.", ".^^", "^..", "..^" -> "^"
                else -> "."
            }
        }
    }.take(times).sumOf { row -> row.count { it == '.' } }
}