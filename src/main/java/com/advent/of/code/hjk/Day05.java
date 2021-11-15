package com.advent.of.code.hjk;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Day05 {

    private Day05() {
        // empty
    }

    public static String part1(String input) {
        return IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> DigestUtils.md5Hex(input + i))
                .filter(v -> v.startsWith("00000"))
                .limit(8)
                .map(v -> String.valueOf(v.charAt(5)))
                .collect(Collectors.joining());
    }

    public static String part2(String input) {
        var password = new ArrayList<>(Collections.nCopies(8, " "));
        var pattern = Pattern.compile("^00000[0-7]");
        for (long i = 0; password.contains(" "); i++) {
            var md5 = DigestUtils.md5Hex(input + i);
            if (pattern.matcher(md5).find()) {
                var index = Integer.parseInt(String.valueOf(md5.charAt(5)));
                if (password.get(index).equals(" ")) {
                    password.set(index, String.valueOf(md5.charAt(6)));
                }
            }
        }
        return String.join("", password);
    }
}
