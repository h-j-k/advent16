package com.advent.of.code.hjk;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Day06 {

    private Day06() {
        // empty
    }

    public static String part1(List<String> input) {
        return solve(input, Comparator.naturalOrder());
    }

    public static String part2(List<String> input) {
        return solve(input, Comparator.reverseOrder());
    }

    private static String solve(List<String> input, Comparator<Long> comparator) {
        return flip(input).stream().map(most(comparator)).collect(Collectors.joining());
    }

    private static List<String> flip(List<String> input) {
        return input.isEmpty() ? Collections.emptyList()
                : IntStream.range(0, input.get(0).length())
                .mapToObj(i -> input.stream().map(line -> String.valueOf(line.charAt(i))).collect(Collectors.joining()))
                .toList();
    }

    private static UnaryOperator<String> most(Comparator<Long> comparator) {
        return value -> value.chars().mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue(comparator))
                .map(Map.Entry::getKey)
                .orElseThrow();
    }
}
