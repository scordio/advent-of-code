package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 1: Calorie Counting")
class Day1Test {

  @Test
  void part1() {
    try (var scanner = new Scanner(getClass().getResourceAsStream("day1"), UTF_8)) {
      int answer = scanner.useDelimiter("\\R\\R").tokens()
        .mapToInt(Day1Test::calculateCalories)
        .max()
        .orElseThrow();

      assertEquals(72511, answer);
    }
  }

  @Test
  void part2() {
    try (var scanner = new Scanner(getClass().getResourceAsStream("day1"), UTF_8)) {
      int answer = scanner.useDelimiter("\\R\\R").tokens()
        .mapToInt(Day1Test::calculateCalories)
        // reverse natural order of integers
        .map(i -> ~i)
        .sorted()
        .map(i -> ~i)
        // ------------------------------------
        .limit(3)
        .sum();

      assertEquals(212117, answer);
    }
  }

  private static int calculateCalories(String block) {
    return block.lines()
      .mapToInt(Integer::parseInt)
      .sum();
  }

}
