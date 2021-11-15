package com.advent.of.code.hjk;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Day04 {

    private Day04() {
        // empty
    }

    private static final Pattern PATTERN = Pattern.compile("(?<name>[a-z-]+)-(?<sectorId>\\d+)\\[(?<checksum>[a-z]+)]");

    public static int part1(List<String> input) {
        return input.stream()
                .map(Day04::parse)
                .filter(Room::isValid)
                .mapToInt(Room::sectorId)
                .sum();
    }

    private static Room parse(String value) {
        var matcher = PATTERN.matcher(value);
        if (matcher.matches()) {
            return new Room(
                    matcher.group("name").replace("-", ""),
                    Integer.parseInt(matcher.group("sectorId")),
                    matcher.group("checksum")
            );
        }
        throw new IllegalArgumentException("Invalid value " + value);
    }

    private record Room(String name, int sectorId, String checksum) {
        private static final Comparator<Map.Entry<String, Long>> COMPARATOR = (a, b) -> {
            var r = b.getValue().compareTo(a.getValue());
            return r == 0 ? a.getKey().compareTo(b.getKey()) : r;
        };

        boolean isValid() {
            return name.chars()
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(COMPARATOR)
                    .limit(5)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.joining())
                    .equals(checksum);
        }
    }
}
