package io.github.scordio.adventofcode2024;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.teeing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.LongStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 1: Historian Hysteria")
class Day1Test {

  @ParameterizedTest
  @CsvSource({
    "day1-example1, 11",
    "day1, 1938424",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Pair::new)
      .collect(teeing(
        mapping(Pair::left, toCollection(PriorityQueue::new)),
        mapping(Pair::right, toCollection(PriorityQueue::new)),
        Day1Test::totalDistance
      ));

    assertEquals(expected, answer);
  }

  private static long totalDistance(PriorityQueue<Integer> left, PriorityQueue<Integer> right) {
    return range(0, left.size())
      .map(_ -> abs(left.remove() - right.remove()))
      .sum();
  }

  @ParameterizedTest
  @CsvSource({
    "day1-example1, 31",
    "day1, 22014209",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Pair::new)
      .collect(teeing(
        mapping(Pair::left, toList()),
        mapping(Pair::right, groupingBy(identity(), counting())),
        Day1Test::totalSimilarityScore
      ));

    assertEquals(expected, answer);
  }

  private static long totalSimilarityScore(List<Integer> left, Map<Integer, Long> right) {
    return left.stream()
      .mapToLong(value -> value * right.getOrDefault(value, 0L))
      .sum();
  }

  private record Pair(int left, int right) {

    Pair(String line) {
      String[] parts = line.split("\\s+");
      this(parseInt(parts[0]), parseInt(parts[1]));
    }

  }

}
