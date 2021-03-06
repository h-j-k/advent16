package com.advent.of.code.hjk;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.advent.of.code.hjk.AdventUtil.combine;
import static java.util.stream.Collectors.toUnmodifiableSet;

public final class Day11 {

    private Day11() {
        // empty
    }

    public static int part1(List<String> input) {
        return process(input.stream().map(Day11::parse).collect(toUnmodifiableSet()));
    }

    public static int part2(List<String> input) {
        var addition = Set.of("elerium", "dilithium");
        var floors = input.stream().map(Day11::parse)
                .map(f -> f.floorIndex > 0 ? f : new Floor(0,
                        mapCombine(f.generators, addition, Generator::new),
                        mapCombine(f.microchips, addition, Microchip::new)
                )).collect(toUnmodifiableSet());
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
        return Stream.concat(initial.stream(), addition.stream().map(mapper)).collect(toUnmodifiableSet());
    }

    private static int process(Set<Floor> floors) {
        Set<String> seen = new HashSet<>();
        for (var queue = new ArrayDeque<>(Set.of(new Move(0, 0, floors))); !queue.isEmpty(); ) {
            var move = queue.removeFirst();
            if (move.isCompleted()) {
                return move.count;
            } else if (seen.add(move.toState())) {
                move.options().forEach(queue::addLast);
            }
        }
        throw new IllegalArgumentException("No moves found.");
    }

    private record Move(int count, int floorIndex, Set<Floor> floors) {
        boolean isCompleted() {
            return floorIndex == floors.stream().mapToInt(f -> f.floorIndex).max().orElseThrow()
                    && floors.stream().allMatch(f -> f.floorIndex == floorIndex || f.isEmpty());
        }

        String toState() {
            return floorIndex + floors.stream().sorted(Comparator.comparing(f -> f.floorIndex)).map(floor -> {
                var pairCount = floor.generators.stream().filter(g -> floor.microchips.stream().anyMatch(g)).count();
                return String.format("%d%d%d", pairCount, floor.generators.size() - pairCount, floor.microchips.size() - pairCount);
            }).collect(Collectors.joining());
        }

        Stream<Move> options() {
            var fromFloor = floors.stream().filter(f -> f.floorIndex == floorIndex).findFirst().orElseThrow();
            return IntStream.of(floorIndex + 1, floorIndex - 1).boxed()
                    .flatMap(i -> floors.stream().filter(f -> f.floorIndex == i).findFirst().stream())
                    .flatMap(fromFloor::to)
                    .map(option -> new Move(count + 1, option.to.floorIndex,
                            floors.stream().map(f -> f.floorIndex == option.from.floorIndex ? option.from
                                    : f.floorIndex == option.to.floorIndex ? option.to : f).collect(toUnmodifiableSet())));
        }
    }

    private record Generator(String element) implements Predicate<Microchip> {
        @Override
        public boolean test(Microchip microchip) {
            return microchip != null && microchip.test(this);
        }
    }

    private record Microchip(String element) implements Predicate<Generator> {
        @Override
        public boolean test(Generator generator) {
            return generator != null && Objects.equals(element, generator.element);
        }
    }

    private record Floor(int floorIndex, Set<Generator> generators, Set<Microchip> microchips) {
        boolean isEmpty() {
            return generators.isEmpty() && microchips.isEmpty();
        }

        boolean isValid() {
            return generators.isEmpty() || microchips.stream().allMatch(m -> generators.stream().anyMatch(m));
        }

        Stream<Option> to(Floor toFloor) {
            var isGoingUp = floorIndex < toFloor.floorIndex;
            return Stream.of(
                    generators.stream().filter(g -> microchips.stream().anyMatch(g)).findFirst().map(option(toFloor)).stream(),
                    options(isGoingUp, generators, toFloor.microchips, moveGenerators(toFloor)),
                    options(isGoingUp, microchips, toFloor.generators, moveMicrochips(toFloor))
            ).reduce(Stream.empty(), Stream::concat).filter(o -> o.from.isValid() && o.to.isValid());
        }

        private Function<Generator, Option> option(Floor to) {
            return generator -> new Option(
                    new Floor(floorIndex,
                            generators.stream().filter(g -> !g.equals(generator)).collect(toUnmodifiableSet()),
                            microchips.stream().filter(m -> !m.test(generator)).collect(toUnmodifiableSet())),
                    new Floor(to.floorIndex,
                            combine(to.generators, Set.of(generator)),
                            combine(to.microchips, Set.of(new Microchip(generator.element)))));
        }

        private static <T extends Predicate<C>, C> Stream<Option> options(boolean isGoingUp,
                                                                          Set<T> values,
                                                                          Set<C> checker,
                                                                          Function<Set<T>, Option> mapper) {
            var copy = values.stream().sorted(Comparator.comparing(v -> checker.stream().noneMatch(v))).toList();
            return (switch (copy.size()) {
                case 0 -> Stream.<Set<T>>empty();
                case 1 -> Stream.of(copy);
                default -> Stream.of(copy.subList(0, 2), copy.subList(0, 1), copy.subList(1, 2));
            }).map(Set::copyOf).filter(set -> isGoingUp || set.size() == 1).map(mapper);
        }

        private Function<Set<Generator>, Option> moveGenerators(Floor to) {
            return moveGenerators -> new Option(
                    new Floor(floorIndex, generators.stream().filter(g -> !moveGenerators.contains(g))
                            .collect(toUnmodifiableSet()), microchips),
                    new Floor(to.floorIndex, combine(to.generators, moveGenerators), to.microchips));
        }

        private Function<Set<Microchip>, Option> moveMicrochips(Floor to) {
            return moveMicrochips -> new Option(
                    new Floor(floorIndex, generators, microchips.stream()
                            .filter(m -> !moveMicrochips.contains(m)).collect(toUnmodifiableSet())),
                    new Floor(to.floorIndex, to.generators, combine(to.microchips, moveMicrochips)));
        }
    }

    private record Option(Floor from, Floor to) {
    }
}
