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
                    var value = parseOrGet(values[1], registers)
                    val offset = parseOrGet(values[2], registers)
                    if (value > 0) {
                        val current = i - 1
                        i = current + offset
                        if (values[1] in "abcd") {
                            val loopOffset =
                                copy.subList(i, current).indexOfFirst { it.matches("(inc|dec) ${values[1]}".toRegex()) }
                            if (loopOffset < 0) continue
                            val multiplier: Int
                            val loopIndex: Int
                            if (offset == -2) {
                                multiplier = 1
                                loopIndex = if (loopOffset == 0) i + 1 else i
                            } else {
                                val inner = copy.subList(i, i + loopOffset)
                                val innerLoopOffset = inner.indexOfFirst { it.matches("jnz.*-2".toRegex()) }
                                val key = copy[i + innerLoopOffset].split(" ")[1]
                                val from = inner.singleOrNull { it.matches("cpy . $key".toRegex()) } ?: continue
                                multiplier = value
                                value = registers.getValue(from.split(" ")[1])
                                val temp = copy.subList(i, i + innerLoopOffset)
                                    .indexOfFirst { it.matches("(inc|dec) $key".toRegex()) }
                                loopIndex = if (temp + 1 == innerLoopOffset) i + temp - 1 else i + temp
                            }
                            val (loopInstruction, loopVariable) = copy[loopIndex].split(" ")
                            if (loopInstruction == "inc") {
                                registers.compute(loopVariable) { _, v -> v!! + value * multiplier }
                                registers[values[1]] = 0
                            } else {
                                registers.compute(loopVariable) { _, v -> v!! - value * multiplier }
                                registers[values[1]] = 0
                            }
                            i = current + 1
                        }
                    }
                }
                "tgl" -> {
                    val target = i - 1 + parseOrGet(values[1], registers)
                    if (target < copy.size && target >= 0) {
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

    fun part2(input: List<String>) = process(input, mapOf("a" to 12, "b" to 0, "c" to 0, "d" to 0))
}