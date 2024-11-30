package io.github.scordio.adventofcode2022;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 1: Calorie Counting")
class Day1Test {

  @ParameterizedTest
  @CsvSource({
    "day1-example, 24000",
    "day1, 72511",
  })
  void part1(@ScanInput(delimiterPattern = "\\R\\R") Scanner scanner, int expected) {
    int answer = scanner.tokens()
      .mapToInt(Day1Test::calculateCalories)
      .max()
      .orElseThrow();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day1-example, 45000",
    "day1, 212117",
  })
  void part2(@ScanInput(delimiterPattern = "\\R\\R") Scanner scanner, int expected) {
    int answer = scanner.tokens()
      .mapToInt(Day1Test::calculateCalories)
      // reverse natural order of integers
      .map(i -> ~i)
      .sorted()
      .map(i -> ~i)
      // ------------------------------------
      .limit(3)
      .sum();

    assertEquals(expected, answer);
  }

  private static int calculateCalories(String block) {
    return block.lines()
      .mapToInt(Integer::parseInt)
      .sum();
  }

}
