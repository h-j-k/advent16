package com.advent.of.code.hjk

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day20Test : TestBase() {

    @Test
    fun part1() {
        assertThat(Day20.part1(input)).isEqualTo(22887907)
    }

    @Test
    fun part2() {
        assertThat(Day20.part2(input)).isEqualTo(109)
    }
}