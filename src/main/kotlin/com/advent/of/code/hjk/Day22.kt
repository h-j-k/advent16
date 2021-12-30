package com.advent.of.code.hjk

import kotlin.math.abs

object Day22 {

    private fun parse(line: String): Node? =
        "/dev/grid/node-x(?<x>\\d+)-y(?<y>\\d+)\\s+(?<size>\\d+)T\\s+(?<used>\\d+)T.+".toRegex()
            .matchEntire(line)?.destructured
            ?.let { (x, y, size, used) -> Node(x.toInt(), y.toInt(), size.toInt(), used.toInt()) }

    fun part1(input: List<String>): Int {
        val nodes = input.drop(2).mapNotNull { parse(it) }
        return nodes.flatMap { node ->
            nodes.mapNotNull { other ->
                setOf(node, other).takeIf { node != other && node.used > 0 && node.used <= other.avail }
            }
        }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val nodes = input.drop(2).mapNotNull { parse(it) }.sortedByDescending { it.avail }
        val maxX = nodes.maxOf { it.x }
        val wall = nodes.filter { it.size > 250 }.minByOrNull { it.x }!!
        val emptyNode = nodes.first { it.used == 0 }
        var result = abs(emptyNode.x - wall.x) + 1
        result += emptyNode.y
        result += maxX - wall.x
        return result + (5 * (maxX - 1)) + 1
    }

    internal data class Node(val x: Int, val y: Int, val size: Int, val used: Int) {
        val avail = size - used
    }
}
