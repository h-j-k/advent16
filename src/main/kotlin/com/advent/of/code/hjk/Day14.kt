package com.advent.of.code.hjk

import org.apache.commons.codec.digest.DigestUtils

private data class Record(val prefix: String, val index: Int, val hash: (String) -> String) {
    val charGroups = charGroups()

    val threeOf = charGroups.firstOrNull { (_, count) -> count >= 3 }?.let { this to it.first }

    val fiveOf = charGroups.mapNotNull { (char, count) -> (char to setOf(index)).takeIf { count >= 5 } }

    private fun charGroups() = hash(prefix + index).fold(mutableListOf<Pair<Char, Int>>()) { acc, c ->
        acc.also {
            if (c == it.lastOrNull()?.first) it[it.lastIndex] = c to it.last().second + 1
            else it += c to 1
        }
    }
}

object Day14 {

    private fun process(input: String, hash: (String) -> String): Int {
        val keys = mutableListOf<Record>()
        val fiveOf = mutableMapOf<Char, Set<Int>>()
        var n = 0
        while (keys.size < 64) {
            Record(input, n, hash).threeOf?.also { (current, threeOf) ->
                val range = ((n + 1)..(n + 1000))
                range.flatMap { Record(input, it, hash).fiveOf }
                    .forEach { (char, index) -> fiveOf.merge(char, index) { a, b -> a + b } }
                if (fiveOf[threeOf]?.any { it in range } == true) keys += current
            }
            n++
        }
        return keys.last().index
    }

    fun part1(input: String) = process(input, DigestUtils::md5Hex)

    fun part2(input: String) = process(input) { s ->
        (1..2016).fold(DigestUtils.md5Hex(s)) { acc, _ -> DigestUtils.md5Hex(acc) }
    }
}