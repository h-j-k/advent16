package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day02Test extends TestBase {
    private final List<String> INPUT = getInput();

    @Test
    public void part1() {
        assertThat(Day02.part1(INPUT)).isEqualTo("76792");
    }
}
