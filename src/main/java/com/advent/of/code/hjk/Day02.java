package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Day02 {

    private Day02() {
        // empty
    }

    public static String part1(List<String> input) {
        var prev = "5";
        var code = new ArrayList<String>();
        for (String line : input) {
            for (String step : line.split("")) {
                prev = Direction.next(prev, Direction.valueOf(step));
            }
            code.add(prev);
        }
        return String.join("", code);
    }

    private enum Direction {
        U, D, L, R;

        private static final Map<String, String> MOVES = moves();

        /*
        1 2 3
        4 5 6
        7 8 9
         */
        private static Map<String, String> moves() {
            var map = new HashMap<String, String>();
            map.put("1", "*4*2");
            map.put("2", "*513");
            map.put("3", "*62*");
            map.put("4", "17*5");
            map.put("5", "2846");
            map.put("6", "395*");
            map.put("7", "4**8");
            map.put("8", "5*79");
            map.put("9", "6*8*");
            return map;
        }

        static String next(String current, Direction direction) {
            return String.valueOf(MOVES.get(current).charAt(direction.ordinal())).replace("*", current);
        }
    }
}
