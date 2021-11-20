package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class Day10 {

    private Day10() {
        // empty
    }

    private static final Pattern VALUE = Pattern.compile("^value (?<value>\\d+) goes to bot (?<id>\\d+)$");
    private static final Pattern GIVE =
            Pattern.compile("^bot (?<id>\\d+) gives low to (?<lowTarget>bot|output) (?<lowId>\\d+) and high to (?<highTarget>bot|output) (?<highId>\\d+)$");

    public static int part1(List<String> input) {
        var bots = new HashMap<Integer, Bot>();
        var outputs = new HashMap<Integer, Integer>();
        var copy = new ArrayList<>(input);
        while (!copy.isEmpty()) {
            var iterator = copy.iterator();
            while (iterator.hasNext()) {
                var instruction = iterator.next();
                var valueMatcher = VALUE.matcher(instruction);
                var giveMatcher = GIVE.matcher(instruction);
                if (valueMatcher.matches()) {
                    var id = Integer.parseInt(valueMatcher.group("id"));
                    var value = Integer.parseInt(valueMatcher.group("value"));
                    bots.merge(id, new Bot(id, value), (a, b) -> a.merge(b.low));
                    iterator.remove();
                } else if (giveMatcher.matches()) {
                    var id = Integer.parseInt(giveMatcher.group("id"));
                    var lowTarget = new Target(giveMatcher.group("lowTarget"), Integer.parseInt(giveMatcher.group("lowId")));
                    var highTarget = new Target(giveMatcher.group("highTarget"), Integer.parseInt(giveMatcher.group("highId")));
                    bots.merge(id, new Bot(id, lowTarget, highTarget, null, null), Bot::merge);
                    if (bots.get(id).isInstructionProcessed(bots, outputs)) {
                        iterator.remove();
                    }
                } else {
                    throw new IllegalStateException("Unknown instruction.");
                }
            }
        }
        return bots.values().stream().filter(bot -> bot.low == 17 && bot.high == 61)
                .findFirst().map(Bot::id).orElseThrow();
    }

    private record Bot(int id, Target lowTarget, Target highTarget, Integer low, Integer high) {

        Bot(int id, int low) {
            this(id, null, null, low, null);
        }

        public Bot merge(Integer value) {
            if (low == null) {
                return new Bot(this.id, this.lowTarget, this.highTarget, value, null);
            } else {
                var newLow = Math.min(low, value);
                var newHigh = high == null ? Math.max(low, value) : Math.max(high, value);
                return new Bot(this.id, this.lowTarget, this.highTarget, newLow, newHigh);
            }
        }

        public Bot merge(Bot update) {
            if (this.id != update.id) {
                throw new IllegalArgumentException("Mismatched id");
            }
            return new Bot(this.id, update.lowTarget, update.highTarget, this.low, this.high);
        }

        boolean isInstructionProcessed(Map<Integer, Bot> bots, Map<Integer, Integer> outputs) {
            if (high == null || !lowTarget.isReady(bots) || !highTarget.isReady(bots)) {
                return false;
            }
            lowTarget.update(bots, outputs, low);
            highTarget.update(bots, outputs, high);
            return true;
        }
    }

    private record Target(String type, int id) {
        boolean isReady(Map<Integer, Bot> bots) {
            return type.equals("output") || bots.containsKey(id);
        }

        void update(Map<Integer, Bot> bots, Map<Integer, Integer> outputs, int value) {
            switch (type) {
                case "bot" -> {
                    if (isReady(bots)) {
                        bots.computeIfPresent(id, (k, bot) -> bot.merge(value));
                    }
                }
                case "output" -> outputs.put(id, value);
            }
        }
    }
}
