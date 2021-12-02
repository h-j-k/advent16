package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day13Test {

    @Test
    fun part1() {
        assertThat(Day13.part1(1352, Day13Coordinate(31, 39))).isEqualTo(90)
    }

    @Test
    fun part2() {
        assertThat(Day13.part2(1352)).isEqualTo(135)
    }
}