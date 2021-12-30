package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day22Test : TestBase() {

    @Test
    fun part1() {
        assertThat(Day22.part1(input)).isEqualTo(903)
    }

    @Test
    fun part2() {
        assertThat(Day22.part2(input)).isEqualTo(215)
    }
}