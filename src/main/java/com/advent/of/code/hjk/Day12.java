package com.advent.of.code.hjk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Day12 {

    private Day12() {
        // empty
    }

    public static int part1(List<String> input) {
        return process(input, Map.of("a", 0, "b", 0, "c", 0, "d", 0));
    }

    public static int part2(List<String> input) {
        return process(input, Map.of("a", 0, "b", 0, "c", 1, "d", 0));
    }

    private static int process(List<String> input, Map<String, Integer> initial) {
        var registers = new HashMap<>(initial);
        for (var i = 0; i < input.size(); ) {
            var values = input.get(i).split(" ");
            switch (values[0]) {
                case "cpy" -> registers.put(values[2], parseOrGet(values[1], registers));
                case "inc" -> registers.compute(values[1], (k, v) -> Objects.requireNonNull(v) + 1);
                case "dec" -> registers.compute(values[1], (k, v) -> Objects.requireNonNull(v) - 1);
                case "jnz" -> {
                    if (parseOrGet(values[1], registers) > 0) {
                        i += Integer.parseInt(values[2]);
                    } else {
                        i++;
                    }
                }
            }
            if (!values[0].equals("jnz")) {
                i++;
            }
        }
        return registers.get("a");
    }

    private static int parseOrGet(String value, Map<String, Integer> registers) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return registers.get(value);
        }
    }
}
