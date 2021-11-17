package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Day08 {

    private Day08() {
        // empty
    }

    public static int part1(List<String> input) {
        Screen.INSTANCE.reset();
        input.forEach(Screen.INSTANCE::process);
        System.out.println(Screen.INSTANCE);
        return Screen.INSTANCE.countLit();
    }

    public static List<String> part2(List<String> input) {
        Screen.INSTANCE.reset();
        input.forEach(Screen.INSTANCE::process);
        return Screen.INSTANCE.contents;
    }

    private enum Screen {
        INSTANCE;

        private static final int MAX_ROW = 6;
        private static final int MAX_COLUMN = 50;
        private static final char LIT = 'x';
        private static final Pattern RECT = Pattern.compile("^rect (?<width>\\d+)x(?<height>\\d+)$");
        private static final Pattern ROW = Pattern.compile("^rotate row y=(?<row>\\d+) by (?<offset>\\d+)$");
        private static final Pattern COLUMN = Pattern.compile("^rotate column x=(?<col>\\d+) by (?<offset>\\d+)$");

        private final List<String> contents = new ArrayList<>(Collections.nCopies(MAX_ROW, make(MAX_COLUMN, ' ')));

        private static String make(int n, char value) {
            return String.join("", Collections.nCopies(n, String.valueOf(value)));
        }

        void reset() {
            contents.clear();
            contents.addAll(Collections.nCopies(MAX_ROW, make(MAX_COLUMN, ' ')));
        }

        void process(String instruction) {
            process(instruction, RECT, this::rect);
            process(instruction, ROW, this::row);
            process(instruction, COLUMN, this::column);
        }

        private void process(String instruction, Pattern pattern, Consumer<Matcher> consumer) {
            Optional.of(instruction).map(pattern::matcher).filter(Matcher::matches).ifPresent(consumer);
        }

        private void rect(Matcher instruction) {
            int width = Integer.parseInt(instruction.group("width"));
            IntStream.range(0, Integer.parseInt(instruction.group("height")))
                    .forEach(r -> contents.set(r, make(width, LIT) + contents.get(r).substring(width)));
        }

        private void row(Matcher instruction) {
            int row = Integer.parseInt(instruction.group("row"));
            int offset = Integer.parseInt(instruction.group("offset"));
            contents.set(row, offset(MAX_COLUMN - offset, contents.get(row)));
        }

        private void column(Matcher instruction) {
            int col = Integer.parseInt(instruction.group("col"));
            int offset = Integer.parseInt(instruction.group("offset"));
            String newColumn = IntStream.range(0, MAX_ROW)
                    .mapToObj(i -> String.valueOf(contents.get(i).charAt(col)))
                    .collect(Collectors.collectingAndThen(Collectors.joining(), v -> offset(MAX_ROW - offset, v)));
            IntStream.range(0, MAX_ROW).forEach(i -> {
                var row = contents.get(i);
                contents.set(i, row.substring(0, col) + newColumn.charAt(i) + row.substring(col + 1));
            });

        }

        private String offset(int offset, String value) {
            return value.substring(offset) + value.substring(0, offset);
        }

        int countLit() {
            return (int) contents.stream().mapToLong(v -> v.chars().filter(c -> c == LIT).count()).sum();
        }
    }
}
