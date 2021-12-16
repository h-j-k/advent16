package com.advent.of.code.hjk

import kotlin.math.pow

object Day20 {

    private fun process(input: List<String>): List<IpDenyList> =
        input.map { line -> line.split("-").let { (from, to) -> IpDenyList(from.toLong(), to.toLong()) } }
            .sortedBy { it.from }.fold(mutableListOf()) { acc, v ->
                acc.also {
                    if (it.isEmpty() || v.from > it.last().to + 1) it.add(v)
                    else it[it.lastIndex] = IpDenyList(it.last().from, it.last().to.coerceAtLeast(v.to))
                }
            }

    fun part1(input: List<String>) = process(input)[0].to + 1

    fun part2(input: List<String>) = process(input).let {
        it.drop(1).fold(it[0] to 0L) { (p, r), v ->
            v to r + v.from - 1 - p.to
        }.second + (2.0.pow(32.0).toLong() - 1 - it.last().to)
    }

    private data class IpDenyList(val from: Long, val to: Long)
}