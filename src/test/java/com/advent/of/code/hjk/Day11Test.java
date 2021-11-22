package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day11Test extends TestBase {

    private final List<String> INPUT = getInput();

    @Test
    public void example() {
        assertThat(Day11.part1(
                List.of(
                        "The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.",
                        "The second floor contains a hydrogen generator.",
                        "The third floor contains a lithium generator.",
                        "The fourth floor contains nothing relevant."
                )
        )).isEqualTo(11);
    }

    @Test
    public void reddit1() {
        assertThat(Day11.part1(
                List.of(
                        "The first floor contains a polonium generator, a thulium generator, a thulium-compatible microchip, a promethium generator, a ruthenium generator, a ruthenium-compatible microchip, a cobalt generator, and a cobalt-compatible microchip.",
                        "The second floor contains a polonium-compatible microchip and a promethium-compatible microchip.",
                        "The third floor contains nothing relevant.",
                        "The fourth floor contains nothing relevant."
                )
        )).isEqualTo(47);
    }

    @Test
    public void reddit2() {
        assertThat(Day11.part2(
                List.of(
                        "The first floor contains a polonium generator, a thulium generator, a thulium-compatible microchip, a promethium generator, a ruthenium generator, a ruthenium-compatible microchip, a cobalt generator, and a cobalt-compatible microchip.",
                        "The second floor contains a polonium-compatible microchip and a promethium-compatible microchip.",
                        "The third floor contains nothing relevant.",
                        "The fourth floor contains nothing relevant."
                )
        )).isEqualTo(71);
    }

    @Test
    public void reddit3() {
        assertThat(Day11.part1(
                List.of(
                        "The first floor contains a strontium generator, a strontium-compatible microchip, a plutonium generator, and a plutonium-compatible microchip.",
                        "The second floor contains a thulium generator, a ruthenium generator, a ruthenium-compatible microchip, a curium generator, and a curium-compatible microchip.",
                        "The third floor contains a thulium-compatible microchip.",
                        "The fourth floor contains nothing relevant."
                )
        )).isEqualTo(37);
    }

    @Test
    public void reddit4() {
        assertThat(Day11.part1(
                List.of(
                        "The first floor contains a promethium generator and a promethium-compatible microchip.",
                        "The second floor contains a cobalt generator, a curium generator, a ruthenium generator, and a plutonium generator.",
                        "The third floor contains a cobalt-compatible microchip, a curium-compatible microchip, a ruthenium-compatible microchip, and a plutonium-compatible microchip.",
                        "The fourth floor contains nothing relevant."
                )
        )).isEqualTo(33);
    }

    @Test
    public void reddit5() {
        assertThat(Day11.part1(
                List.of(
                        "The first floor contains a promethium generator and a promethium-compatible microchip.",
                        "The second floor contains a cobalt generator, a curium generator, and a plutonium generator.",
                        "The third floor contains a cobalt-compatible microchip, a curium-compatible microchip, and a plutonium-compatible microchip.",
                        "The fourth floor contains nothing relevant."
                )
        )).isEqualTo(25);
    }


    @Test
    public void part1() {
        assertThat(Day11.part1(INPUT)).isEqualTo(31);
    }

    @Test
    public void part2() {
        assertThat(Day11.part2(INPUT)).isEqualTo(55);
    }
}
