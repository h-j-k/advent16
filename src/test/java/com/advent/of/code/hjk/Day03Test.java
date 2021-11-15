package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day03Test extends TestBase {

    private final List<String> INPUT = getInput();

    @Test
    public void part1() {
        assertThat(Day03.part1(INPUT)).isEqualTo(869);
    }

    @Test
    public void part2() {
        assertThat(Day03.part2(INPUT)).isEqualTo(1544);
    }
}
