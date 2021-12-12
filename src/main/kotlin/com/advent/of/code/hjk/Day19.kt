package com.advent.of.code.hjk

object Day19 {

    fun part1(input: Int): Int {
        var elves = (1..input).associateWith { 1 }.toMutableMap()
        while (elves.size > 1) {
            val next = mutableMapOf<Int, Int>()
            var iterator = elves.iterator()
            while (iterator.hasNext()) {
                val (index, value) = iterator.next()
                iterator.remove()
                if (iterator.hasNext()) {
                    val (_, nextValue) = iterator.next()
                    iterator.remove()
                    next[index] = value + nextValue
                } else {
                    iterator = next.iterator()
                    val (_, nextValue) = iterator.next()
                    iterator.remove()
                    next[index] = value + nextValue
                    break
                }
            }
            elves = next
        }
        return elves.keys.first()
    }
}