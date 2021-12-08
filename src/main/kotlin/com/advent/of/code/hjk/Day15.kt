package com.advent.of.code.hjk

object Day15 {

    private data class Disc(val n: Int, val count: Int, val start: Int) {
        fun zeroesAt(time: Int) = (time + n + start) % count == 0
    }

    private fun parse(line: String): Disc? =
        "Disc #(?<n>\\d+) has (?<count>\\d+) positions; at time=0, it is at position (?<start>\\d+).".toRegex()
            .matchEntire(line)?.destructured?.let { (n, count, start) -> Disc(n.toInt(), count.toInt(), start.toInt()) }

    private fun process(discs: List<Disc>): Int {
        var t = 0
        while (!discs.all { it.zeroesAt(t) }) {
            t++
        }
        return t
    }

    fun part1(input: List<String>): Int = process(input.mapNotNull(Day15::parse))

    fun part2(input: List<String>): Int = process(input.mapNotNull(Day15::parse).let { it + Disc(it.size + 1, 11, 0) })
}