package com.advent.of.code.hjk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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

    private static final List<String> ORDINALS = List.of("first", "second", "third", "fourth");
    private static final Pattern DELIMITER = Pattern.compile("[,.]|and");
    private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

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
        int floorIndex = IntStream.range(0, ORDINALS.size())
                .filter(i -> floor.startsWith(String.format("The %s floor contains", ORDINALS.get(i))))
                .findFirst()
                .orElseThrow();
        var generators = new HashSet<Generator>();
        var microchips = new HashSet<Microchip>();
        DELIMITER.splitAsStream(floor).filter(v -> !v.trim().isEmpty()).forEach(part -> {
            String[] values = SPACE_PATTERN.splitAsStream(part.trim()).toArray(String[]::new);
            String element = values[values.length - 2];
            switch (values[values.length - 1]) {
                case "generator" -> generators.add(new Generator(element.intern()));
                case "microchip" -> microchips.add(new Microchip(element.replace("-compatible", "").intern()));
            }
        });
        return new Floor(floorIndex, generators, microchips);
    }

    private static <T> Set<T> mapCombine(Set<T> initial, Set<String> addition, Function<String, T> mapper) {
        return Stream.concat(initial.stream(), addition.stream().map(mapper)).collect(Collectors.toUnmodifiableSet());
    }

    private static int process(List<Floor> floors) {
        return process(new Result(), new Elevator(0, Direction.UP), floors, 0).orElseThrow();
    }

    private static OptionalInt process(Result result, Elevator elevator, List<Floor> floors, int currentMoves) {
        if (result.isTerminating(elevator, floors, currentMoves)) {
            return OptionalInt.empty();
        } else if (result.recordAndIsBestMoves(elevator, floors, currentMoves)) {
            return OptionalInt.of(currentMoves);
        }
        var fromFloor = floors.get(elevator.floorIndex);
        return (elevator.areLowerFloorsEmpty(floors) ? IntStream.of(elevator.floorIndex + 1)
                : IntStream.of(elevator.floorIndex + 1, elevator.floorIndex - 1))
                .filter(i -> i >= 0 && i < floors.size())
                .mapToObj(floors::get)
                .flatMap(fromFloor::to)
                .parallel()
                .filter(o -> o.from.isValid() && o.to.isValid())
                .map(o -> process(result, o.elevator(), o.copyUpdate(floors), currentMoves + 1))
                .flatMapToInt(OptionalInt::stream)
                .min();
    }

    private interface Element {
        String element();
    }

    private record Generator(String element) implements Predicate<Microchip>, Element {
        @Override
        public boolean test(Microchip microchip) {
            return microchip.element.equals(element);
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
            var pair = generators.stream()
                    .filter(g -> microchips.stream().anyMatch(g)).findAny();
            var direction = floorIndex < toFloor.floorIndex ? Direction.UP : Direction.DOWN;
            return Stream.of(
                    pair.map(Generator::element).map(pairOption(toFloor)).stream(),
                    optionsFor(direction, generators, toFloor.microchips, moveGenerators(toFloor)),
                    optionsFor(direction, microchips, toFloor.generators, moveMicrochips(toFloor))
            ).reduce(Stream.empty(), Stream::concat);
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
                                                                     Set<? extends Element> toFilter,
                                                                     Function<Set<T>, Option> mapper) {
            var elements = toFilter.stream().map(Element::element).collect(Collectors.toUnmodifiableSet());
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

    private record Elevator(int floorIndex, Direction direction) {
        boolean areLowerFloorsEmpty(List<Floor> floors) {
            return floors.subList(0, floorIndex).stream().allMatch(Floor::isEmpty);
        }
    }

    private enum Direction {UP, DOWN}

    private record Option(Floor from, Floor to) {
        Elevator elevator() {
            return new Elevator(to.floorIndex, from.floorIndex < to.floorIndex ? Direction.UP : Direction.DOWN);
        }

        List<Floor> copyUpdate(List<Floor> floors) {
            var updatedFloors = new ArrayList<>(floors);
            updatedFloors.set(from.floorIndex, from);
            updatedFloors.set(to.floorIndex, to);
            return updatedFloors;
        }
    }

    private static final class Result {
        private final Map<String, Integer> seen = new ConcurrentHashMap<>();
        private final AtomicInteger bestMoves = new AtomicInteger(Integer.MAX_VALUE);

        boolean isTerminating(Elevator elevator, List<Floor> floors, int currentMoves) {
            return bestMoves.get() <= currentMoves || Optional.of(toState(elevator, floors))
                    .filter(state -> seen.containsKey(state) && seen.get(state) <= currentMoves)
                    .isPresent();
        }

        boolean recordAndIsBestMoves(Elevator elevator, List<Floor> floors, int currentMoves) {
            if (elevator.areLowerFloorsEmpty(floors) && elevator.floorIndex == floors.size() - 1) {
                bestMoves.set(currentMoves);
                return true;
            }
            seen.merge(toState(elevator, floors), currentMoves, Integer::min);
            return bestMoves.get() == currentMoves;
        }

        private static String toState(Elevator elevator, List<Floor> floors) {
            return toState(elevator) + floors.stream().map(Result::toState).collect(Collectors.joining("", ":", ""));
        }

        private static String toState(Elevator elevator) {
            var goingUp = elevator.floorIndex + ">";
            var goingDown = "<" + elevator.floorIndex;
            return elevator.floorIndex == 0 ? goingUp
                    : elevator.floorIndex == ORDINALS.size() - 1 ? goingDown
                    : switch (elevator.direction) {
                case UP -> goingUp;
                case DOWN -> goingDown;
            };
        }

        private static String toState(Floor floor) {
            if (floor.isEmpty()) {
                return "[]";
            }
            var pairCount = (int) floor.generators.stream().filter(g -> floor.microchips.stream().anyMatch(g)).count();
            return String.format("[%d:%d:%d]", pairCount, floor.generators.size() - pairCount, floor.microchips.size() - pairCount);
        }
    }
}
