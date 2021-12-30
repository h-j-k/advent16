package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day24Test : TestBase() {

    @Test
    fun part1() {
        assertThat(Day24.part1(input)).isEqualTo(456)
    }

    @Test
    fun part2() {
        assertThat(Day24.part2(input)).isEqualTo(704)
    }
}