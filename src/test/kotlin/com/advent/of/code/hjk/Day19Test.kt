package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day19Test {

    @Test
    fun part1() {
        assertThat(Day19.part1(3001330)).isEqualTo(1808357)
    }

    @Test
    fun part2() {
        assertThat(Day19.part2(3001330)).isEqualTo(1407007)
    }
}