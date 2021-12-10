package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day17Test {

    @Test
    fun part1() {
        assertThat(Day17.part1("ioramepc")).isEqualTo("RDDRULDDRR")
    }

    @Test
    fun part2() {
        assertThat(Day17.part2("ioramepc")).isEqualTo(766)
    }
}
