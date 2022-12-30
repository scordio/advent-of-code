package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 2: Rock Paper Scissors")
class Day2Test {

  @Test
  void part1() {
    var scores = Map.of(
      "A X", 4,
      "A Y", 8,
      "A Z", 3,
      "B X", 1,
      "B Y", 5,
      "B Z", 9,
      "C X", 7,
      "C Y", 2,
      "C Z", 6
    );

    try (var scanner = new Scanner(getClass().getResourceAsStream("day2"), UTF_8)) {
      int answer = scanner.useDelimiter("\\R").tokens()
        .mapToInt(scores::get)
        .sum();

      assertEquals(15572, answer);
    }
  }

  @Test
  void part2() {
    var scores = Map.of(
      "A X", 3,
      "A Y", 4,
      "A Z", 8,
      "B X", 1,
      "B Y", 5,
      "B Z", 9,
      "C X", 2,
      "C Y", 6,
      "C Z", 7
    );

    try (var scanner = new Scanner(getClass().getResourceAsStream("day2"), UTF_8)) {
      int answer = scanner.useDelimiter("\\R").tokens()
        .mapToInt(scores::get)
        .sum();

      assertEquals(16098, answer);
    }
  }

}
