package com.advent.of.code.hjk

import java.util.ArrayDeque

object Day24 {

    private fun parse(input: List<String>): List<List<Position>> = input.indices.map { y ->
        input[0].indices.map { x -> Position(x = x, y = y, value = input[y][x].toString()) }
    }

    private fun Position.from(other: Position, board: List<List<Position>>): Int {
        val seen = mutableMapOf(this to 0)
        val queue = ArrayDeque<Position>().also { it.add(this) }
        while (queue.isNotEmpty()) {
            val current = queue.pop()
            val distance = seen.getValue(current)
            if (current == other) return distance
            listOf(
                board[current.y][current.x - 1],
                board[current.y][current.x + 1],
                board[current.y - 1][current.x],
                board[current.y + 1][current.x]
            ).filter { it.isValid && !it.isWall && it !in seen }.let { neighbors ->
                seen.putAll(neighbors.map { it to distance + 1 })
                queue.addAll(neighbors)
            }
        }
        return 0
    }

    private fun mapDistances(map: List<List<Position>>): Map<Pair<Int, Int>, Int> {
        val targets = map.flatMap { r -> r.mapNotNull { p -> p.n?.let { it to p } } }.toMap()
        return targets.flatMap { (target, position) ->
            targets.filter { it.key > target }.flatMap { (otherTarget, otherPosition) ->
                val distance = position.from(otherPosition, map)
                listOf(target to otherTarget, otherTarget to target).map { it to distance }
            }
        }.toMap()
    }

    private fun shortestPath(map: List<List<Position>>, isReturning: Boolean): Int {
        val (targets, distances) = mapDistances(map).let { it.keys.flatMap { (a, b) -> listOf(a, b) }.toSet() to it }
        fun move(leftover: Set<Int>, current: Int, steps: Int): Int =
            if (leftover.isEmpty()) steps + (distances.getValue(current to 0).takeIf { isReturning } ?: 0)
            else targets.filter { it in leftover }
                .minOf { move(leftover - it, it, steps + distances.getValue(current to it)) }
        return move(targets - 0, 0, 0)
    }

    fun part1(input: List<String>): Int = shortestPath(parse(input), false)

    fun part2(input: List<String>): Int = shortestPath(parse(input), true)

    private data class Position(val x: Int, val y: Int, val value: String) {
        val isValid = x >= 0 && y >= 0
        val isWall = value == "#"
        val n = try {
            value.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }
}