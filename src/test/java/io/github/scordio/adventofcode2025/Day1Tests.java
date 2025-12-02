package io.github.scordio.adventofcode2025;

import io.github.scordio.ScanInput;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Tests {

  @ParameterizedTest
  @CsvSource({
    "day1-example1, 3",
    "day1, 1150",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    Safe safe = new Safe();

    scanner.tokens().forEach(safe::rotate);

    assertEquals(expected, safe.zeroPositions);
  }

  @ParameterizedTest
  @CsvSource({
    "day1-example1, 6",
    "day1, 6738",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    Safe safe = new Safe();

    scanner.tokens().forEach(safe::rotate);

    assertEquals(expected, safe.zeroClicks);
  }

  static class Safe {

    private int currentPosition = 50;

    int zeroPositions;
    int zeroClicks;

    void rotate(String value) {
      int clicks = parse(value);

      zeroClicks += abs(clicks) / 100;

      int remainder = clicks % 100;

      if (remainder != 0) {
        int nextPosition = currentPosition + remainder;

        if (currentPosition / 100 != nextPosition / 100 || currentPosition != 0 && currentPosition > 0 != nextPosition > 0) {
          ++zeroClicks;
        }

        currentPosition = (nextPosition + 100) % 100;
      }

      if (currentPosition == 0) {
        ++zeroPositions;
      }
    }

    private static int parse(String input) {
      int value = parseInt(input.substring(1));
      return input.charAt(0) == 'R' ? value : -value;
    }

  }

}
