package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Day07 {

    private Day07() {
        // empty
    }

    private static final Predicate<String> IS_ABBA =
            v -> v.charAt(0) == v.charAt(3) && v.charAt(1) == v.charAt(2) && v.charAt(0) != v.charAt(1);

    public static int part1(List<String> input) {
        return solve(input, (supernet, hypernet) ->
                supernet.stream().anyMatch(isAbba(true)) && hypernet.stream().allMatch(isAbba(false))
        );
    }

    public static int part2(List<String> input) {
        return solve(input, (supernet, hypernet) -> {
            var patterns = supernet.stream().flatMap(Day07::filterAba).toList();
            var reversedPatterns = patterns.stream()
                    .map(v -> new String(new char[]{v.charAt(1), v.charAt(0), v.charAt(1)}))
                    .collect(Collectors.toSet());
            return hypernet.stream().flatMap(Day07::filterAba).anyMatch(reversedPatterns::contains);
        });
    }

    private static int solve(List<String> input, BiPredicate<List<String>, List<String>> biPredicate) {
        return (int) input.stream().filter(value -> {
            var supernet = new ArrayList<String>();
            var hypernet = new ArrayList<String>();
            var isHypernet = value.startsWith("[");
            for (String part : chop(value)) {
                if (isHypernet) {
                    hypernet.add(part);
                } else {
                    supernet.add(part);
                }
                isHypernet = !isHypernet;
            }
            return biPredicate.test(supernet, hypernet);
        }).count();
    }

    public static List<String> chop(String value) {
        return Pattern.compile("[^a-z]").splitAsStream(value).toList();
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

    private static Stream<String> filterAba(String value) {
        if (value.length() < 3) {
            return Stream.empty();
        }
        return IntStream.range(0, value.length() - 2)
                .mapToObj(i -> value.substring(i, i + 3))
                .filter(v -> v.charAt(0) == v.charAt(2) && v.charAt(0) != v.charAt(1));
    }
}
