package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public final class Day07 {

    private Day07() {
        // empty
    }

    private static final Pattern PATTERN = Pattern.compile("[^a-z]");

    private static final Predicate<String> IS_ABBA =
            v -> v.charAt(0) == v.charAt(3) && v.charAt(1) == v.charAt(2) && v.charAt(0) != v.charAt(1);

    public static int part1(List<String> input) {
        return (int) input.stream().filter(value -> {
            var regular = new ArrayList<String>();
            var hypernet = new ArrayList<String>();
            var isHypernet = false;
            for (String part : chop(value)) {
                if (isHypernet) {
                    hypernet.add(part);
                } else {
                    regular.add(part);
                }
                isHypernet = !isHypernet;
            }
            return regular.stream().anyMatch(isAbba(true))
                    && hypernet.stream().allMatch(isAbba(false));
        }).count();
    }

    public static List<String> chop(String value) {
        return PATTERN.splitAsStream(value).toList();
    }

    private static Predicate<String> isAbba(boolean isMatching) {
        return value -> {
            if (value.length() < 4) {
                return false;
            }
            var stream = IntStream.range(0, value.length() - 3)
                    .mapToObj(i -> value.substring(i, i + 4));
            return isMatching ? stream.anyMatch(IS_ABBA) : stream.noneMatch(IS_ABBA);
        };
    }
}
