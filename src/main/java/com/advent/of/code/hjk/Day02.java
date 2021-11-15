package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Day02 {

    private Day02() {
        // empty
    }

    private static final String DIRECTION = "UDLR";

    private static Map<String, String> part1Moves() {
            /*
            1 2 3
            4 5 6
            7 8 9
            */
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

    private static Map<String, String> part2Moves() {
            /*
                1
              2 3 4
            5 6 7 8 9
              A B C
                D
            */
        var map = new HashMap<String, String>();
        map.put("1", "*3**");
        map.put("2", "*6*3");
        map.put("3", "1724");
        map.put("4", "*83*");
        map.put("5", "***6");
        map.put("6", "2A57");
        map.put("7", "3B68");
        map.put("8", "4C79");
        map.put("9", "**8*");
        map.put("A", "6**B");
        map.put("B", "7DAC");
        map.put("C", "8*B*");
        map.put("D", "B***");
        return map;
    }

    public static String part1(List<String> input) {
        return solve(part1Moves(), input);
    }

    public static String part2(List<String> input) {
        return solve(part2Moves(), input);
    }

    public static String solve(Map<String, String> moves, List<String> input) {
        var prev = "5";
        var code = new ArrayList<String>();
        for (String line : input) {
            for (String step : line.split("")) {
                prev = String.valueOf(
                        moves.get(prev).charAt(DIRECTION.indexOf(step))
                ).replace("*", prev);
            }
            code.add(prev);
        }
        return String.join("", code);
    }
}
