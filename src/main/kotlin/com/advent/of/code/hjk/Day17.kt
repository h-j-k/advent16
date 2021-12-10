package com.advent.of.code.hjk

import org.apache.commons.codec.digest.DigestUtils

/*
#########
#S| | | #
#-#-#-#-#
# | | | #
#-#-#-#-#
# | | | #
#-#-#-#-#
# | | |
####### V
 */

private data class Day17Move(val sequence: String, val x: Int, val y: Int) {
    val isComplete = x == 3 && y == 3

    val nextMoves: List<Day17Move>
        get() = if (isComplete || x !in 0..3 || y !in 0..3) emptyList()
        else DigestUtils.md5Hex(sequence).substring(0, 4).mapIndexedNotNull { index, c ->
            if (c !in "bcdef") null else Day17Move(
                sequence = "$sequence${"UDLR"[index]}",
                x = x + (if (index < 2) 0 else index * 2 - 5),
                y = y + (if (index > 1) 0 else index * 2 - 1)
            )
        }
}

object Day17 {

    private fun process(input: String): Set<String> {
        val queue = ArrayDeque(setOf(Day17Move(input, 0, 0)))
        val candidates = mutableListOf<String>()
        while (queue.isNotEmpty()) {
            queue.removeFirst()
                .also { if (it.isComplete) candidates += it.sequence.substring(input.length) }
                .nextMoves.forEach(queue::addLast)
        }
        return candidates.toSet()
    }

    fun part1(input: String) = process(input).minByOrNull { it.length }!!

    fun part2(input: String) = process(input).maxOf { it.length }
}
