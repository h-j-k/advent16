package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Day03 {

    private Day03() {
        // empty
    }

    private static final Pattern PATTERN = Pattern.compile("\\s+");

    public static int part1(List<String> input) {
        return countValidTriangles(
                input.stream().map(line ->
                        PATTERN.splitAsStream(line)
                                .filter(v -> !v.isEmpty())
                                .map(Integer::parseInt)
                                .toList()
                ));
    }

    public static int part2(List<String> input) {
        var temp = input.stream().map(line ->
                PATTERN.splitAsStream(line)
                        .filter(v -> !v.isEmpty())
                        .map(Integer::parseInt)
                        .toList()
        ).toList();
        var flipped = new ArrayList<List<Integer>>();
        for (int i = 0; i < temp.size(); i += 3) {
            var a = temp.get(i);
            var b = temp.get(i + 1);
            var c = temp.get(i + 2);
            IntStream.range(0, 3).forEach(j ->
                    flipped.add(List.of(a.get(j), b.get(j), c.get(j)))
            );
        }
        return countValidTriangles(flipped.stream());
    }

    private static int countValidTriangles(Stream<List<Integer>> stream) {
        return (int) stream.filter(Day03::isValidTriangle).count();
    }

    private static boolean isValidTriangle(List<Integer> values) {
        if (values.size() != 3) {
            throw new IllegalArgumentException("Not 3 values");
        }
        return values.stream().max(Comparator.naturalOrder())
                .map(max -> values.stream().reduce(0, Integer::sum) > 2 * max)
                .orElse(false);
    }
}
