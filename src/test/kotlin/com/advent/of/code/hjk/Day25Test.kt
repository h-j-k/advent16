package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day25Test : TestBase() {

    @Test
    fun part1() {
        assertThat(Day25.part1(input)).isEqualTo(198)
    }
}