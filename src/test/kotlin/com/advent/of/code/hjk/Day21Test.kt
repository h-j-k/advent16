package com.advent.of.code.hjk

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day21Test : TestBase() {

    @Test
    fun part1() {
        assertThat(Day21.part1("abcdefgh", input)).isEqualTo("hcdefbag")
    }

    @Test
    fun part2() {
        assertThat(Day21.part2("fbgdceah", input)).isEqualTo("fbhaegdc")
    }
}