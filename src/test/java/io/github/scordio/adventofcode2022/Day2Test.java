package io.github.scordio.adventofcode2022;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;
import java.util.function.ToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 2: Rock Paper Scissors")
class Day2Test {

  @ParameterizedTest
  @CsvSource({
    "day2-example, 15",
    "day2, 15572",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
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

    int answer = scanner.tokens()
      .mapToInt(scores)
      .sum();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day2-example, 12",
    "day2, 16098",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
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

    int answer = scanner.tokens()
      .mapToInt(scores)
      .sum();

    assertEquals(expected, answer);
  }

}
