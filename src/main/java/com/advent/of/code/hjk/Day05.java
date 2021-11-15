package com.advent.of.code.hjk;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Day05 {

    private Day05() {
        // empty
    }

    public static String part1(String input) {
        return generate(input);
    }

    private static String generate(String prefix) {
        return IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> DigestUtils.md5Hex(prefix + i))
                .filter(v -> v.startsWith("00000"))
                .limit(8)
                .map(v -> String.valueOf(v.charAt(5)))
                .collect(Collectors.joining());
    }
}
