package com.advent.of.code.hjk

object Day23 {

    private fun process(input: List<String>, initial: Map<String, Int>): Int {
        val copy = input.toMutableList()
        val registers = initial.toMutableMap()
        var i = 0
        while (i < copy.size) {
            val values = copy[i].split(" ")
            i++
            when (values[0]) {
                "cpy" -> values[2].takeIf { it in "abcd" }?.let { registers[it] = parseOrGet(values[1], registers) }
                "inc" -> registers.compute(values[1]) { _, v -> v!! + 1 }
                "dec" -> registers.compute(values[1]) { _, v -> v!! - 1 }
                "jnz" -> {
                    if (parseOrGet(values[1], registers) > 0) {
                        i += parseOrGet(values[2], registers) - 1
                    }
                }
                "tgl" -> {
                    val target = i - 1 + parseOrGet(values[1], registers)
                    if (target < copy.size) {
                        val newInstruction = when (copy[target].count { it == ' ' }) {
                            1 -> if (copy[target].startsWith("inc")) "dec" else "inc"
                            2 -> if (copy[target].startsWith("jnz")) "cpy" else "jnz"
                            else -> values[0]
                        }
                        copy[target] = copy[target].replaceRange(0, 3, newInstruction)
                    }
                }
            }
        }
        return registers.getValue("a")
    }

    private fun parseOrGet(value: String, registers: Map<String, Int>) = try {
        value.toInt()
    } catch (e: NumberFormatException) {
        registers.getValue(value)
    }

    fun part1(input: List<String>) = process(input, mapOf("a" to 7, "b" to 0, "c" to 0, "d" to 0))
}