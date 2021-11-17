package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day08Test extends TestBase {

    private final List<String> INPUT = getInput();

    @Test
    public void part1() {
        assertThat(Day08.part1(INPUT)).isEqualTo(106);
    }

    @Test
    public void part2() {
        // CFLELOYFCS
        assertThat(Day08.part2(INPUT)).containsExactly(
                " xx  xxxx x    xxxx x     xx  x   xxxxx  xx   xxx ",
                "x  x x    x    x    x    x  x x   xx    x  x x    ",
                "x    xxx  x    xxx  x    x  x  x x xxx  x    x    ",
                "x    x    x    x    x    x  x   x  x    x     xx  ",
                "x  x x    x    x    x    x  x   x  x    x  x    x ",
                " xx  x    xxxx xxxx xxxx  xx    x  x     xx  xxx  "
        );
    }
}
