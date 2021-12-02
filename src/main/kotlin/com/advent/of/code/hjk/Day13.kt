package com.advent.of.code.hjk

import kotlin.math.abs

data class Day13Coordinate(val x: Int, val y: Int) {
    init {
        require(x >= 0 && y >= 0)
    }

    private fun isOpen(n: Int): Boolean =
        ((x * x + 3 * x + 2 * x * y + y + y * y) + n)
            .toString(2).count { it == '1' } % 2 == 0

    fun movesAround(n: Int): Set<Day13Coordinate> = (-1..1).flatMap { xDelta ->
        (-1..1).mapNotNull { yDelta ->
            if (x + xDelta >= 0 && y + yDelta >= 0 && (abs(xDelta) + abs(yDelta)) == 1)
                copy(x = x + xDelta, y = y + yDelta)
            else null
        }
    }.filter { it.isOpen(n) }.toSet()
}

private data class Move(val count: Int, val position: Day13Coordinate)

object Day13 {
    fun part1(n: Int, target: Day13Coordinate): Int {
        val seen = mutableSetOf<Day13Coordinate>()
        val queue = ArrayDeque(setOf(Move(0, Day13Coordinate(1, 1))))
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current.position == target) {
                return current.count
            } else if (seen.add(current.position)) {
                current.position.movesAround(n)
                    .map { Move(current.count + 1, it) }
                    .let(queue::addAll)
            }
        }
        throw IllegalArgumentException("Unable to reach $target")
    }

    fun part2(n: Int): Int {
        val seen = mutableSetOf<Day13Coordinate>()
        val queue = ArrayDeque(setOf(Move(0, Day13Coordinate(1, 1))))
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (seen.add(current.position) && current.count + 1 <= 50) {
                current.position.movesAround(n)
                    .map { Move(current.count + 1, it) }
                    .let(queue::addAll)
            }
        }
        return seen.size
    }
}