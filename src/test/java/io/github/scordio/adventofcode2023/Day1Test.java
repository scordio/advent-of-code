package io.github.scordio.adventofcode2023;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 1: Trebuchet?!")
class Day1Test {

  private static final Pattern DIGIT_OR_WORD = compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");

  @ParameterizedTest
  @CsvSource({
    "day1-example1, 142",
    "day1, 54644",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    ToIntFunction<String> toCalibrationValue = value -> {
      int[] digits = value.chars()
        .filter(Character::isDigit)
        .map(character -> character - '0')
        .toArray();

      return digits[0] * 10 + digits[digits.length - 1];
    };

    long answer = scanner.tokens()
      .mapToInt(toCalibrationValue)
      .sum();

    assertEquals(expected, answer);

  }

  @ParameterizedTest
  @CsvSource({
    "day1-example2, 281",
    "day1-example3, 31",
    "day1, 53348",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    ToIntFunction<String> toCalibrationValue = value -> getFirst(value) * 10 + getLast(value);

    long answer = scanner.tokens()
      .mapToInt(toCalibrationValue)
      .sum();

    assertEquals(expected, answer);
  }

  private static int getFirst(String value) {
    Matcher matcher = DIGIT_OR_WORD.matcher(value);

    if (matcher.find()) return parseInt(matcher.group());

    throw new IllegalArgumentException();
  }

  private static int getLast(String value) {
    Matcher matcher = DIGIT_OR_WORD.matcher(value);

    for (int i = value.length() - 1; i >= 0; --i) {
      if (matcher.find(i)) {
        return parseInt(matcher.group());
      }
    }

    throw new IllegalArgumentException();
  }

  private static int parseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return switch (value) {
        case "one" -> 1;
        case "two" -> 2;
        case "three" -> 3;
        case "four" -> 4;
        case "five" -> 5;
        case "six" -> 6;
        case "seven" -> 7;
        case "eight" -> 8;
        case "nine" -> 9;
        default -> throw new IllegalArgumentException();
      };
    }
  }

}
