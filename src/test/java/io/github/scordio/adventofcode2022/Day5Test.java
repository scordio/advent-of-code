package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Day 5: Supply Stacks")
class Day5Test {

  static Stream<Arguments> part1() {
    return Stream.of(
      arguments("day5-example", "CMZ", List.of(
        new ArrayDeque<>(List.of("N", "Z")),
        new ArrayDeque<>(List.of("D", "C", "M")),
        new ArrayDeque<>(List.of("P")))),
      arguments("day5", "FWNSHLDNZ", List.of(
        new ArrayDeque<>(List.of("N", "W", "F", "R", "Z", "S", "M", "D")),
        new ArrayDeque<>(List.of("S", "G", "Q", "P", "W")),
        new ArrayDeque<>(List.of("C", "J", "N", "F", "Q", "V", "R", "W")),
        new ArrayDeque<>(List.of("L", "D", "G", "C", "P", "Z", "F")),
        new ArrayDeque<>(List.of("S", "P", "T")),
        new ArrayDeque<>(List.of("L", "R", "W", "F", "D", "H")),
        new ArrayDeque<>(List.of("C", "D", "N", "Z")),
        new ArrayDeque<>(List.of("Q", "J", "S", "V", "F", "R", "N", "W")),
        new ArrayDeque<>(List.of("V", "W", "Z", "G", "S", "M", "R"))))
    );
  }

  @ParameterizedTest
  @MethodSource
  void part1(String input, String expected, List<Deque<String>> stacks) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        if (!line.startsWith("move")) continue;

        String[] parts = line.substring(5).split(" from | to ");
        int quantity = parseInt(parts[0]);
        Deque<String> from = stacks.get(parseInt(parts[1]) - 1);
        Deque<String> to = stacks.get(parseInt(parts[2]) - 1);

        range(0, quantity)
          .mapToObj(i -> from.removeFirst())
          .forEach(to::addFirst);
      }

      String answer = stacks.stream()
        .map(Deque::peek)
        .collect(joining());

      assertEquals(expected, answer);
    }
  }

  static Stream<Arguments> part2() {
    return Stream.of(
      arguments("day5-example", "MCD", List.of(
        new ArrayDeque<>(List.of("N", "Z")),
        new ArrayDeque<>(List.of("D", "C", "M")),
        new ArrayDeque<>(List.of("P")))),
      arguments("day5", "RNRGDNFQG", List.of(
        new ArrayDeque<>(List.of("N", "W", "F", "R", "Z", "S", "M", "D")),
        new ArrayDeque<>(List.of("S", "G", "Q", "P", "W")),
        new ArrayDeque<>(List.of("C", "J", "N", "F", "Q", "V", "R", "W")),
        new ArrayDeque<>(List.of("L", "D", "G", "C", "P", "Z", "F")),
        new ArrayDeque<>(List.of("S", "P", "T")),
        new ArrayDeque<>(List.of("L", "R", "W", "F", "D", "H")),
        new ArrayDeque<>(List.of("C", "D", "N", "Z")),
        new ArrayDeque<>(List.of("Q", "J", "S", "V", "F", "R", "N", "W")),
        new ArrayDeque<>(List.of("V", "W", "Z", "G", "S", "M", "R"))))
    );
  }

  @ParameterizedTest
  @MethodSource
  void part2(String input, String expected, List<Deque<String>> stacks) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        if (!line.startsWith("move")) continue;

        String[] parts = line.substring(5).split(" from | to ");
        int quantity = parseInt(parts[0]);
        Deque<String> from = stacks.get(parseInt(parts[1]) - 1);
        Deque<String> to = stacks.get(parseInt(parts[2]) - 1);

        range(0, quantity)
          .mapToObj(i -> from.removeFirst())
          .collect(toCollection(ArrayDeque::new))
          .descendingIterator()
          .forEachRemaining(to::addFirst);
      }

      String answer = stacks.stream()
        .map(Deque::peek)
        .collect(joining());

      assertEquals(expected, answer);
    }
  }

}
