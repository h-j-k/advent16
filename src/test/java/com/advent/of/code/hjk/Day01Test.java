package com.advent.of.code.hjk;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class Day01Test extends TestBase {

    private final List<String> INPUT =
            getInput().stream().flatMap(line -> Pattern.compile(", ").splitAsStream(line)).toList();

    @Test
    public void part1() {
        assertThat(Day01.part1(INPUT)).isEqualTo(242);
    }

    @Test
    public void part2() {
        assertThat(Day01.part2(INPUT)).isEqualTo(150);
    }
}