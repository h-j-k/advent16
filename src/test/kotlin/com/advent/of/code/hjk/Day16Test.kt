package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day16Test {

    @Test
    fun part1() {
        assertThat(Day16.part1(272, "10001001100000001")).isEqualTo("10101001010100001")
    }

    @Test
    fun part2() {
        assertThat(Day16.part1(35651584, "10001001100000001")).isEqualTo("10100001110101001")
    }
}