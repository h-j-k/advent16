package com.advent.of.code.hjk

import java.util.ArrayDeque

object Day19 {

    fun part1(input: Int): Int {
        val queue = ArrayDeque((1..input).toList())
        while (queue.size > 1) {
            queue.add(queue.pop())
            queue.pop()
        }
        return queue.first()
    }

    fun part2(input: Int): Int {
        val left = ArrayDeque((1..input / 2).toList())
        val right = ArrayDeque(((input / 2) + 1..input).toList())
        while (left.size + right.size > 1) {
            if (left.size > right.size) left.pollLast() else right.pollFirst()
            right.addLast(left.pollFirst())
            left.addLast(right.pollFirst())
        }
        return left.firstOrNull() ?: right.first()
    }
}