package com.advent.of.code.hjk;

import java.util.Optional;
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
        var length = 0L;
        Marker marker = null;
        for (int i = 0; i < input.length(); ) {
            if (marker == null) {
                if (input.charAt(i) == '(') {
                    marker = parseMarker(i, input);
                    i += String.format("(%dx%d)", marker.size, marker.times).length();
                } else {
                    length++;
                    i++;
                }
            } else {
                length += (isNested ? process(input.substring(i, i + marker.size), true) : marker.size) * marker.times;
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

    private record Marker(int size, int times) {
    }
}
