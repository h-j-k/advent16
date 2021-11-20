package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day10Test extends TestBase {

    private final List<String> INPUT = getInput();

    @Test
    public void part1() {
        assertThat(Day10.part1(INPUT)).isEqualTo(56);
    }

    @Test
    public void part2() {
        assertThat(Day10.part2(INPUT)).isEqualTo(7847);
    }
}
