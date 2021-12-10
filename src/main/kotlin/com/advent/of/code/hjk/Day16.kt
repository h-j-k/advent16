package com.advent.of.code.hjk

object Day16 {

    private fun fill(length: Int, input: String): String = generateSequence(input) {
        if (it.length >= length) null else "${it}0${
            it.reversed().replace('0', '2').replace('1', '0').replace('2', '1')
        }"
    }.last()

    private val String.checksum: String
        get() = generateSequence(this) {
            if (it.length % 2 == 1) null else it.chunked(2) { chunk ->
                when (chunk) {
                    "00", "11" -> "1"
                    "01", "10" -> "0"
                    else -> throw IllegalArgumentException()
                }
            }.joinToString("")
        }.last()

    fun part1(length: Int, input: String): String {
        return fill(length, input).substring(0, length).checksum
    }
}