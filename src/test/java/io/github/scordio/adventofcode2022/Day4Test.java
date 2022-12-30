package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.BitSet;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 4: Camp Cleanup")
class Day4Test {

  @ParameterizedTest
  @CsvSource({
    "day4, 524",
    "day4-example, 2"
  })
  void part1(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      long answer = scanner.useDelimiter("\\R").tokens()
        .map(RangePair::new)
        .filter(RangePair::isOneFullyContained)
        .count();

      assertEquals(expected, answer);
    }
  }

  @ParameterizedTest
  @CsvSource({
    "day4, 798",
    "day4-example, 4"
  })
  void part2(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      long answer = scanner.useDelimiter("\\R").tokens()
        .map(RangePair::new)
        .filter(RangePair::areOverlapping)
        .count();

      assertEquals(expected, answer);
    }
  }

  static class RangePair {

    private final Range left;
    private final Range right;

    RangePair(String line) {
      var ranges = line.split(",");
      left = new Range(ranges[0]);
      right = new Range(ranges[1]);
    }

    boolean isOneFullyContained() {
      return left.contains(right) || right.contains(left);
    }

    boolean areOverlapping() {
      return left.intersects(right);
    }

    private static class Range {

      private final BitSet bitSet = new BitSet();

      private Range(String range) {
        String[] bounds = range.split("-");
        int lowerBound = parseInt(bounds[0]) - 1;
        int upperBound = parseInt(bounds[1]);
        bitSet.set(lowerBound, upperBound);
      }

      private boolean contains(Range input) {
        return input.bitSet.stream().allMatch(bitSet::get);
      }

      private boolean intersects(Range input) {
        return bitSet.intersects(input.bitSet);
      }

    }

  }


}
