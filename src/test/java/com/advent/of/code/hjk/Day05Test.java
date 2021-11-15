package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Day05Test {

    @Test
    public void part1() {
        assertThat(Day05.part1("uqwqemis")).isEqualTo("1a3099aa");
    }

    @Test
    public void part2() {
        assertThat(Day05.part2("uqwqemis")).isEqualTo("694190cd");
    }
}
