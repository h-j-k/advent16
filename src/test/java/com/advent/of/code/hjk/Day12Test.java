package com.advent.of.code.hjk;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day12Test extends TestBase {

    private final List<String> INPUT = getInput();

    @Test
    public void part1() {
        assertThat(Day12.part1(INPUT)).isEqualTo(318077);
    }

    @Disabled // takes quite long, a minute...?
    @Test
    public void part2() {
        assertThat(Day12.part2(INPUT)).isEqualTo(9227731);
    }
}
