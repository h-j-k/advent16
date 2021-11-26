package com.advent.of.code.hjk;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.advent.of.code.hjk.AdventUtil.combine;

public final class Day11 {

    private Day11() {
        // empty
    }

    public static int part1(List<String> input) {
        return process(input.stream().map(Day11::parse).sorted().toList());
    }

    public static int part2(List<String> input) {
        var addition = Set.of("elerium", "dilithium");
        var floors = input.stream().map(Day11::parse)
                .map(f -> f.floorIndex > 0 ? f : new Floor(0,
                        mapCombine(f.generators, addition, Generator::new),
                        mapCombine(f.microchips, addition, Microchip::new)
                )).sorted().toList();
        return process(floors);
    }

    private static Floor parse(String floor) {
        var ordinals = List.of("first", "second", "third", "fourth");
        var floorIndex = IntStream.range(0, ordinals.size())
                .filter(i -> floor.startsWith(String.format("The %s floor contains", ordinals.get(i))))
                .findFirst()
                .orElseThrow();
        var generators = new HashSet<Generator>();
        var microchips = new HashSet<Microchip>();
        var pattern = Pattern.compile(" (?<element>[^ -]+)[ -](?<type>generator|compatible microchip)");
        for (var matcher = pattern.matcher(floor); matcher.find(); ) {
            if (matcher.group("type").equals("generator")) {
                generators.add(new Generator(matcher.group("element")));
            } else {
                microchips.add(new Microchip(matcher.group("element")));
            }
        }
        return new Floor(floorIndex, Set.copyOf(generators), Set.copyOf(microchips));
    }

    private static <T> Set<T> mapCombine(Set<T> initial, Set<String> addition, Function<String, T> mapper) {
        return Stream.concat(initial.stream(), addition.stream().map(mapper)).collect(Collectors.toUnmodifiableSet());
    }

    private static int process(List<Floor> floors) {
        int bestMoves = Integer.MAX_VALUE;
        Map<String, Integer> seen = new HashMap<>();
        var queue = floors.get(0).to(floors.get(1))
                .map(option -> Move.from(1, floors, option))
                .collect(Collectors.toCollection(ArrayDeque::new));
        while (!queue.isEmpty()) {
            var move = queue.removeFirst();
            if (move.count < bestMoves) {
                var state = move.toState();
                if (Optional.ofNullable(seen.get(state)).filter(v -> v <= move.count).isEmpty()) {
                    if (move.isCompleted()) {
                        bestMoves = move.count;
                    } else if (seen.merge(state, move.count, Integer::min) == move.count) {
                        move.options().forEach(queue::addLast);
                    }
                }
            }
        }
        return bestMoves;
    }

    private record Move(int count, int floorIndex, Direction direction, List<Floor> floors) {
        static Move from(int count, List<Floor> oldFloors, Option option) {
            return new Move(count, option.to.floorIndex,
                    option.from.compareTo(option.to) < 0 ? Direction.UP : Direction.DOWN,
                    oldFloors.stream().map(f -> f.floorIndex == option.from.floorIndex ? option.from
                            : f.floorIndex == option.to.floorIndex ? option.to : f).toList());
        }

        boolean isCompleted() {
            return floorIndex == floors.size() - 1 && floors.subList(0, floorIndex).stream().allMatch(Floor::isEmpty);
        }

        String toState() {
            return IntStream.concat(
                    floors.stream().flatMapToInt(Move::toState),
                    IntStream.of(0, floorIndex, direction.ordinal())
            ).mapToObj(String::valueOf).collect(Collectors.joining());
        }

        private static IntStream toState(Floor floor) {
            if (floor.isEmpty()) {
                return IntStream.of(0, 0, 0);
            }
            var pairCount = (int) floor.generators.stream().filter(g -> floor.microchips.stream().anyMatch(g)).count();
            return IntStream.of(pairCount, floor.generators.size() - pairCount, floor.microchips.size() - pairCount);
        }

        Stream<Move> options() {
            var fromFloor = floors.get(floorIndex);
            return IntStream.of(floorIndex + 1, floorIndex - 1)
                    .filter(i -> i >= 0 && i < floors.size())
                    .mapToObj(floors::get)
                    .flatMap(fromFloor::to)
                    .map(option -> from(count + 1, floors, option));
        }
    }

    private interface Element {
        String element();
    }

    private record Generator(String element) implements Predicate<Microchip>, Element {
        @Override
        public boolean test(Microchip microchip) {
            return microchip.test(this);
        }
    }

    private record Microchip(String element) implements Predicate<Generator>, Element {
        @Override
        public boolean test(Generator generator) {
            return generator.element.equals(element);
        }
    }

    private record Floor(int floorIndex,
                         Set<Generator> generators,
                         Set<Microchip> microchips) implements Comparable<Floor> {
        @Override
        public int compareTo(Floor otherFloor) {
            return Integer.compare(floorIndex, otherFloor.floorIndex);
        }

        boolean isEmpty() {
            return generators.isEmpty() && microchips.isEmpty();
        }

        boolean isValid() {
            return isEmpty() || generators.isEmpty()
                    || microchips.stream().allMatch(m -> generators.stream().anyMatch(m));
        }

        Stream<Option> to(Floor toFloor) {
            var pair = generators.stream().filter(g -> microchips.stream().anyMatch(g)).findAny();
            var direction = floorIndex < toFloor.floorIndex ? Direction.UP : Direction.DOWN;
            return Stream.of(
                    pair.map(Generator::element).map(pairOption(toFloor)).stream(),
                    optionsFor(direction, generators, toFloor.microchips, moveGenerators(toFloor)),
                    optionsFor(direction, microchips, toFloor.generators, moveMicrochips(toFloor))
            ).reduce(Stream.empty(), Stream::concat).filter(o -> o.from.isValid() && o.to.isValid());
        }

        Function<String, Option> pairOption(Floor to) {
            return element -> new Option(
                    new Floor(floorIndex,
                            generators.stream().filter(g -> !g.element.equals(element))
                                    .collect(Collectors.toUnmodifiableSet()),
                            microchips.stream().filter(m -> !m.element.equals(element))
                                    .collect(Collectors.toUnmodifiableSet())),
                    new Floor(to.floorIndex,
                            combine(to.generators, Set.of(new Generator(element))),
                            combine(to.microchips, Set.of(new Microchip(element))))
            );
        }

        private static <T extends Element> Stream<Option> optionsFor(Direction direction,
                                                                     Set<T> toCheck,
                                                                     Set<? extends Element> withFilter,
                                                                     Function<Set<T>, Option> mapper) {
            var elements = withFilter.stream().map(Element::element).collect(Collectors.toUnmodifiableSet());
            var copy = new ArrayList<T>(toCheck.size());
            toCheck.forEach(element -> {
                if (elements.contains(element.element())) {
                    copy.add(0, element);
                } else {
                    copy.add(element);
                }
            });
            return (switch (copy.size()) {
                case 0 -> Stream.<Set<T>>empty();
                case 1 -> Stream.of(copy);
                default -> direction == Direction.UP
                        ? Stream.of(copy.subList(0, 2), copy.subList(0, 1), copy.subList(1, 2))
                        : Stream.of(copy.subList(0, 1), copy.subList(1, 2));
            }).map(Set::copyOf).map(mapper);
        }

        private Function<Set<Generator>, Option> moveGenerators(Floor to) {
            return moveGenerators -> new Option(
                    new Floor(floorIndex, generators.stream().filter(g -> !moveGenerators.contains(g))
                            .collect(Collectors.toUnmodifiableSet()), microchips),
                    new Floor(to.floorIndex, combine(to.generators, moveGenerators), to.microchips)
            );
        }

        private Function<Set<Microchip>, Option> moveMicrochips(Floor to) {
            return moveMicrochips -> new Option(
                    new Floor(floorIndex, generators, microchips.stream()
                            .filter(m -> !moveMicrochips.contains(m)).collect(Collectors.toUnmodifiableSet())),
                    new Floor(to.floorIndex, to.generators, combine(to.microchips, moveMicrochips))
            );
        }
    }

    private record Option(Floor from, Floor to) {
    }

    private enum Direction {UP, DOWN}
}
