package com.advent.of.code.hjk

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day14Test {

    @Test
    fun part1() {
        Assertions.assertThat(Day14.part1("zpqevtbw")).isEqualTo(16106)
    }

    @Disabled // 20 mins...!
    @Test
    fun part2() {
        Assertions.assertThat(Day14.part2("zpqevtbw")).isEqualTo(22423)
    }
}