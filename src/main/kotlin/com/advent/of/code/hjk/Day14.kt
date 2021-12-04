package com.advent.of.code.hjk

import org.apache.commons.codec.digest.DigestUtils

private data class Record(val prefix: String, val index: Int, val hash: (String) -> String) {
    val charGroups = charGroups()

    val threeOf = charGroups.firstOrNull { (_, count) -> count >= 3 }?.first

    val fiveOf = charGroups.mapNotNull { (char, count) -> char.takeIf { count >= 5 } }.associateWith { setOf(index) }

    private fun charGroups(): List<Pair<Char, Int>> {
        val list = mutableListOf<Pair<Char, Int>>()
        val hash = hash(prefix + index)
        var entry: Pair<Char, Int> = hash[0] to 1
        for (c in hash.substring(1)) {
            entry = when (c) {
                entry.first -> c to (entry.second + 1)
                else -> {
                    list += entry
                    c to 1
                }
            }
        }
        list += entry
        return list
    }
}

object Day14 {

    private fun process(input: String, hash: (String) -> String): Int {
        val keys = mutableListOf<Record>()
        val fiveOf = mutableMapOf<Char, Set<Int>>()
        var n = 0
        while (keys.size != 64) {
            val current = Record(input, n, hash)
            current.threeOf?.also { threeOf ->
                val range = ((n + 1)..(n + 1000))
                range.map { Record(input, it, hash).fiveOf }.forEach {
                    it.forEach { (char, index) -> fiveOf.merge(char, index) { a, b -> a + b } }
                }
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