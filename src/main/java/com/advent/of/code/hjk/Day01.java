package com.advent.of.code.hjk;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public final class Day01 {
    private Day01() {
        // empty
    }

    public static int part1(List<String> input) {
        var position = new Position(Direction.N, new Point(0, 0));
        for (String instruction : input) {
            position = position.next(instruction);
        }
        return position.point.getDistance();
    }

    public static int part2(List<String> input) {
        var position = new Position(Direction.N, new Point(0, 0));
        var seen = new HashSet<Point>();
        for (String instruction : input) {
            Position temp = position.next(instruction);
            for (Point point : position.point.range(temp.point)) {
                if (!seen.add(point)) {
                    return point.getDistance();
                }
            }
            position = temp;
        }
        throw new IllegalStateException("Nothing visited twice");
    }

    private enum Direction {
        N, S, E, W;

        public Direction left() {
            return switch (this) {
                case N -> W;
                case S -> E;
                case E -> N;
                case W -> S;
            };
        }

        public Direction right() {
            return switch (this) {
                case N -> E;
                case S -> W;
                case E -> S;
                case W -> N;
            };
        }

        public Point next(Point point, int offset) {
            return switch (this) {
                case N -> new Point(point.x, point.y + offset);
                case S -> new Point(point.x, point.y - offset);
                case E -> new Point(point.x + offset, point.y);
                case W -> new Point(point.x - offset, point.y);
            };
        }
    }

    private record Point(int x, int y) {
        int getDistance() {
            return Math.abs(x) + Math.abs(y);
        }

        List<Point> range(Point other) {
            if (x == other.x) {
                return y < other.y
                        ? IntStream.range(y, other.y).mapToObj(i -> new Point(x, i)).toList()
                        : IntStream.range(-1 * other.y, -1 * y).mapToObj(i -> new Point(x, -1 * i)).toList();
            } else if (y == other.y) {
                return x < other.x
                        ? IntStream.range(x, other.x).mapToObj(i -> new Point(i, y)).toList()
                        : IntStream.range(-1 * other.x, -1 * x).mapToObj(i -> new Point(-1 * i, y)).toList();
            } else {
                throw new IllegalArgumentException("Incorrect range");
            }
        }
    }

    private record Position(Direction direction, Point point) {
        private static final Pattern PATTERN = Pattern.compile("(?<turn>[LR])(?<offset>\\d+)");

        public Position next(String instruction) {
            Matcher matcher = PATTERN.matcher(instruction);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Unmatched instruction " + instruction + ".");
            }
            int offset = Integer.parseInt(matcher.group("offset"));
            Direction nextDirection = switch (matcher.group("turn")) {
                case "L" -> direction.left();
                case "R" -> direction.right();
                default -> throw new IllegalArgumentException("Unexpected turn.");
            };
            return new Position(nextDirection, nextDirection.next(point, offset));
        }
    }
}
