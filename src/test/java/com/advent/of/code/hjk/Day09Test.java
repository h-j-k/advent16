package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Day09Test extends TestBase {

    private final String INPUT = getInput().get(0);

    @Test
    public void part1() {
        assertThat(Day09.part1(INPUT)).isEqualTo(183269);
    }
}
