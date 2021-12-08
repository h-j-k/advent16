package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day15Test : TestBase() {

    @Test
    fun part1() {
        assertThat(Day15.part1(input)).isEqualTo(121834)
    }

    @Test
    fun part2() {
        assertThat(Day15.part2(input)).isEqualTo(3208099)
    }
}