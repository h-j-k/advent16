package com.advent.of.code.hjk;

import java.util.Collections;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public final class Day09 {

    private Day09() {
        // empty
    }

    public static int part1(String input) {
        var stringBuilder = new StringBuilder();
        Marker marker = null;
        for (int i = 0; i < input.length(); ) {
            if (marker == null) {
                if (input.charAt(i) == '(') {
                    marker = parseMarker(i, input);
                    i += marker.begin();
                } else {
                    stringBuilder.append(input.charAt(i));
                    i++;
                }
            } else {
                stringBuilder.append(marker.apply(input.substring(i)));
                i += marker.size;
                marker = null;
            }
        }
        return stringBuilder.length();
    }

    private static final Pattern PATTERN = Pattern.compile("(?<size>\\d+)x(?<times>\\d+)");

    private static Marker parseMarker(int index, String input) {
        var endIndex = input.indexOf(')', index + 1);
        if (endIndex == -1) {
            throw new IllegalStateException("No closing ): " + input.substring(index));
        }
        var matcher = PATTERN.matcher(input.substring(index + 1, endIndex));
        if (!matcher.matches()) {
            throw new IllegalStateException("Not a marker: " + input.substring(index + 1, endIndex));
        }
        return new Marker(Integer.parseInt(matcher.group("size")), Integer.parseInt(matcher.group("times")));
    }

    private record Marker(int size, int times) implements UnaryOperator<String> {
        int begin() {
            return String.format("%dx%d", size, times).length() + 2;
        }

        @Override
        public String apply(String input) {
            return String.join("", Collections.nCopies(times, input.substring(0, size)));
        }
    }
}
