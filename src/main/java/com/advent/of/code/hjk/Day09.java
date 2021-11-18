package com.advent.of.code.hjk;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day09 {

    private Day09() {
        // empty
    }

    public static long part1(String input) {
        return process(input, false);
    }

    public static long part2(String input) {
        return process(input, true);
    }

    private static long process(String input, boolean isNested) {
        long length = 0;
        Marker marker = null;
        for (int i = 0; i < input.length(); ) {
            if (marker == null) {
                if (input.charAt(i) == '(') {
                    marker = parseMarker(i, input);
                    i += marker.begin();
                } else {
                    length++;
                    i++;
                }
            } else {
                String substring = input.substring(i, i + marker.size);
                length += isNested ? process(substring, true) * marker.times : marker.apply(substring);
                i += marker.size;
                marker = null;
            }
        }
        return length;
    }

    private static final Pattern PATTERN = Pattern.compile("(?<size>\\d+)x(?<times>\\d+)");

    private static Marker parseMarker(int index, String input) {
        return Optional.of(PATTERN.matcher(input.substring(index + 1, input.indexOf(')', index + 1))))
                .filter(Matcher::matches)
                .map(m -> new Marker(Integer.parseInt(m.group("size")), Integer.parseInt(m.group("times"))))
                .orElseThrow();
    }

    private record Marker(int size, int times) implements Function<String, Integer> {
        int begin() {
            return String.format("%dx%d", size, times).length() + 2;
        }

        @Override
        public Integer apply(String input) {
            return String.join("", Collections.nCopies(times, input.substring(0, size))).length();
        }
    }
}
