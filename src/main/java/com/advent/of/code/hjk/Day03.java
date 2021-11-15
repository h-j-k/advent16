package com.advent.of.code.hjk;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public final class Day03 {

    private Day03() {
        // empty
    }

    private static final Pattern PATTERN = Pattern.compile("\\s+");

    public static int part1(List<String> input) {
        return (int) input.stream().filter(Day03::isValidTriangle).count();
    }

    private static boolean isValidTriangle(String line) {
        var values = PATTERN.splitAsStream(line)
                .filter(v -> !v.isEmpty())
                .map(Integer::parseInt)
                .toList();
        return values.stream().max(Comparator.naturalOrder())
                .map(max -> values.stream().reduce(0, Integer::sum) > 2 * max)
                .orElse(false);
    }
}
