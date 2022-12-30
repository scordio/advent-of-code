package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;
import java.util.function.ToIntFunction;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 2: Rock Paper Scissors")
class Day2Test {

  @Test
  void part1() {
    ToIntFunction<String> scores = line -> switch (line) {
      case "A X" -> 4;
      case "A Y" -> 8;
      case "A Z" -> 3;
      case "B X" -> 1;
      case "B Y" -> 5;
      case "B Z" -> 9;
      case "C X" -> 7;
      case "C Y" -> 2;
      case "C Z" -> 6;
      default -> throw new IllegalArgumentException(line);
    };

    try (var scanner = new Scanner(getClass().getResourceAsStream("day2"), UTF_8)) {
      int answer = scanner.useDelimiter("\\R").tokens()
        .mapToInt(scores)
        .sum();

      assertEquals(15572, answer);
    }
  }

  @Test
  void part2() {
    ToIntFunction<String> scores = line -> switch (line) {
      case "A X" -> 3;
      case "A Y" -> 4;
      case "A Z" -> 8;
      case "B X" -> 1;
      case "B Y" -> 5;
      case "B Z" -> 9;
      case "C X" -> 2;
      case "C Y" -> 6;
      case "C Z" -> 7;
      default -> throw new IllegalArgumentException(line);
    };

    try (var scanner = new Scanner(getClass().getResourceAsStream("day2"), UTF_8)) {
      int answer = scanner.useDelimiter("\\R").tokens()
        .mapToInt(scores)
        .sum();

      assertEquals(16098, answer);
    }
  }

}
