package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day18Test : TestBase() {

    @Test
    fun part1() {
        assertThat(Day18.part1(input[0], 40)).isEqualTo(1961)
    }

    @Test
    fun part2() {
        assertThat(Day18.part1(input[0], 400_000)).isEqualTo(20000795)
    }
}