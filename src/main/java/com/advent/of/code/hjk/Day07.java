package com.advent.of.code.hjk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Day07 {

    private Day07() {
        // empty
    }

    private static final Predicate<String> IS_ABBA =
            value -> window(4, value).anyMatch(v -> v[0] == v[3] && v[1] == v[2] && v[0] != v[1]);

    private static final Function<String, Stream<String>> FILTER_ABA =
            value -> window(3, value).filter(v -> v[0] == v[2] && v[0] != v[1]).map(String::new);

    public static int part1(List<String> input) {
        return solve(input, address ->
                address.supernetStream().anyMatch(IS_ABBA) && address.hypernetStream().noneMatch(IS_ABBA)
        );
    }

    public static int part2(List<String> input) {
        return solve(input, address -> {
            var reversedPatterns = address.supernetStream().flatMap(FILTER_ABA)
                    .map(v -> v.substring(1) + v.charAt(1))
                    .collect(Collectors.toUnmodifiableSet());
            return address.hypernetStream().flatMap(FILTER_ABA).anyMatch(reversedPatterns::contains);
        });
    }

    private static int solve(List<String> input, Predicate<Address> predicate) {
        return (int) input.stream().filter(value -> {
            var supernet = new HashSet<String>();
            var hypernet = new HashSet<String>();
            var isHypernet = value.startsWith("[");
            for (String part : value.split("[^a-z]")) {
                (isHypernet ? hypernet : supernet).add(part);
                isHypernet = !isHypernet;
            }
            return predicate.test(new Address(Set.copyOf(supernet), Set.copyOf(hypernet)));
        }).count();
    }

    private static Stream<char[]> window(int size, String value) {
        return IntStream.range(0, value.length() - size + 1).mapToObj(i -> value.substring(i, i + size).toCharArray());
    }

    private record Address(Set<String> supernet, Set<String> hypernet) {
        Stream<String> supernetStream() {
            return supernet.stream();
        }

        Stream<String> hypernetStream() {
            return hypernet.stream();
        }
    }
}
