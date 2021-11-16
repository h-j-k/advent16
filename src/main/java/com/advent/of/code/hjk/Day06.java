package com.advent.of.code.hjk;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Day06 {

    private Day06() {
        // empty
    }

    public static String part1(List<String> input) {
        return flip(input).stream().map(Day06::most).collect(Collectors.joining());
    }

    private static List<String> flip(List<String> input) {
        return input.isEmpty() ? Collections.emptyList()
                : IntStream.range(0, input.get(0).length())
                .mapToObj(i -> input.stream().map(line -> String.valueOf(line.charAt(i))).collect(Collectors.joining()))
                .toList();
    }

    private static String most(String value) {
        return value.chars().mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
    }
}
