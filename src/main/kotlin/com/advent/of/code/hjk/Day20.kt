package com.advent.of.code.hjk

import kotlin.math.pow

object Day20 {

    private fun grouped(input: List<String>): List<IpDenyList> =
        input.map { line -> line.split("-").let { (from, to) -> IpDenyList(from.toLong(), to.toLong()) } }
            .sortedBy { it.from }
            .runningReduce { prev, current ->
                if (current.from <= prev.to + 1) IpDenyList(prev.from, prev.to.coerceAtLeast(current.to)) else current
            }

    fun part1(input: List<String>): Long {
        val grouped = grouped(input)
        var last = grouped[0]
        for (g in grouped) {
            if (g.from != last.from) return last.to + 1
            last = g
        }
        return Long.MIN_VALUE
    }

    fun part2(input: List<String>): Long {
        val grouped = grouped(input)
        var last = grouped[0]
        var count = 0L
        for (g in grouped) {
            if (g.from != last.from) count += g.from - 1 - last.to
            last = g
        }
        Int.MAX_VALUE
        return count + (2.0.pow(32.0).toLong() - 1 - last.to)
    }

    private data class IpDenyList(val from: Long, val to: Long)
}