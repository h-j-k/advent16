package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day06Test extends TestBase {

    private final List<String> INPUT = getInput();

    @Test
    public void part1() {
        assertThat(Day06.part1(INPUT)).isEqualTo("cyxeoccr");
    }

    @Test
    public void part2() {
        assertThat(Day06.part2(INPUT)).isEqualTo("batwpask");
    }
}
