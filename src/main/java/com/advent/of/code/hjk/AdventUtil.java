package com.advent.of.code.hjk;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AdventUtil {

    private AdventUtil() {
        // empty
    }

    public static <T> Set<T> combine(Set<T> a, Set<T> b) {
        return Stream.of(a, b).flatMap(Set::stream).collect(Collectors.toUnmodifiableSet());
    }
}
