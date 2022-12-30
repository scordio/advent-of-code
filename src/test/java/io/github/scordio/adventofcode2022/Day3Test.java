package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 3: Rucksack Reorganization")
class Day3Test {

  @ParameterizedTest
  @CsvSource({
    "day3, 8123",
    "day3-example, 157"
  })
  void part1(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      int answer = scanner.useDelimiter("\\R").tokens()
        .mapToInt(Day3Test::calculateRucksackPriority)
        .sum();

      assertEquals(expected, answer);
    }
  }

  private static int calculateRucksackPriority(String line) {
    var firstCompartment = line.substring(0, line.length() / 2);
    var secondCompartment = line.substring(line.length() / 2);
    return firstCompartment.chars()
      .filter(item -> secondCompartment.indexOf(item) >= 0)
      .map(Day3Test::calculateItemPriority)
      .findAny()
      .orElseThrow();
  }

  @ParameterizedTest
  @CsvSource({
    "day3, 2620",
    "day3-example, 70"
  })
  void part2(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      int answer = 0;

      while (scanner.hasNextLine()) {
        var firstRucksack = scanner.nextLine();
        var secondRucksack = scanner.nextLine();
        var thirdRucksack = scanner.nextLine();

        answer += firstRucksack.chars()
          .filter(item -> secondRucksack.indexOf(item) >= 0 && thirdRucksack.indexOf(item) >= 0)
          .map(Day3Test::calculateItemPriority)
          .findAny()
          .orElseThrow();
      }

      assertEquals(expected, answer);
    }
  }

  private static int calculateItemPriority(int character) {
    return Character.isLowerCase((char) character)
      ? character - 'a' + 1
      : character - 'A' + 27;
  }

}
