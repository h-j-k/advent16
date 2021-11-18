package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Day08 {

    private Day08() {
        // empty
    }

    public static int part1(List<String> input) {
        return (int) process(input).stream().mapToLong(v -> v.chars().filter(c -> c == LIT).count()).sum();
    }

    public static List<String> part2(List<String> input) {
        return process(input);
    }

    private static final int MAX_ROW = 6;
    private static final int MAX_COLUMN = 50;
    private static final char LIT = 'x';

    private static List<String> process(List<String> input) {
        var contents = new ArrayList<>(Collections.nCopies(MAX_ROW, make(MAX_COLUMN, ' ')));
        input.stream().flatMap(instruction ->
                EnumSet.allOf(Updater.class).stream().flatMap(v -> Map.of(v, instruction).entrySet().stream())
        ).forEach(entry -> entry.getKey().accept(contents, entry.getValue()));
        return List.copyOf(contents);
    }

    private static String make(int n, char value) {
        return String.join("", Collections.nCopies(n, String.valueOf(value)));
    }

    private enum Updater implements BiConsumer<List<String>, String> {
        RECT(Pattern.compile("^rect (?<width>\\d+)x(?<height>\\d+)$"), contents -> instruction -> {
            var width = Integer.parseInt(instruction.group("width"));
            IntStream.range(0, Integer.parseInt(instruction.group("height")))
                    .forEach(update(contents, (i, row) -> make(width, LIT) + row.substring(width)));
        }),
        ROW(Pattern.compile("^rotate row y=(?<row>\\d+) by (?<offset>\\d+)$"), contents -> instruction -> {
            var offset = MAX_COLUMN - Integer.parseInt(instruction.group("offset"));
            IntStream.of(Integer.parseInt(instruction.group("row")))
                    .forEach(update(contents, (i, row) -> offset(offset, row)));
        }),
        COLUMN(Pattern.compile("^rotate column x=(?<col>\\d+) by (?<offset>\\d+)$"), contents -> instruction -> {
            var col = Integer.parseInt(instruction.group("col"));
            var offset = MAX_ROW - Integer.parseInt(instruction.group("offset"));
            var updated = IntStream.range(0, MAX_ROW)
                    .mapToObj(i -> String.valueOf(contents.get(i).charAt(col)))
                    .collect(Collectors.collectingAndThen(Collectors.joining(), v -> offset(offset, v)));
            IntStream.range(0, MAX_ROW).forEach(update(contents, (i, row) ->
                    row.substring(0, col) + updated.charAt(i) + row.substring(col + 1)));
        });

        private final Pattern pattern;
        private final Function<List<String>, Consumer<Matcher>> mapper;

        Updater(Pattern pattern, Function<List<String>, Consumer<Matcher>> mapper) {
            this.pattern = pattern;
            this.mapper = mapper;
        }

        @Override
        public void accept(List<String> contents, String instruction) {
            Optional.of(pattern.matcher(instruction)).filter(Matcher::matches).ifPresent(mapper.apply(contents));
        }

        private static String offset(int offset, String value) {
            return value.substring(offset) + value.substring(0, offset);
        }

        private static IntConsumer update(List<String> contents, BiFunction<Integer, String, String> updater) {
            return i -> contents.set(i, updater.apply(i, contents.get(i)));
        }
    }
}
