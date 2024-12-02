package io.github.scordio.adventofcode2024;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.signum;
import static java.lang.Math.abs;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 2: Red-Nosed Reports")
class Day2Test {

  @ParameterizedTest
  @CsvSource({
    "day2-example1, 2",
    "day2, 236",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Report::new)
      .filter(Report::isSafe)
      .count();

    assertEquals(expected, answer);
  }

  @Disabled("not implemented")
  @ParameterizedTest
  @CsvSource({
    "day2-example1, 4",
    "day2, -1",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    long answer = 0;

    assertEquals(expected, answer);
  }

  private static class Report {

    private final List<Integer> levels;

    Report(String input) {
      this.levels = stream(input.split(" "))
        .map(Integer::valueOf)
        .toList();
    }

    boolean isSafe() {
      int difference = levels.get(1) - levels.get(0);
      int abs = abs(difference);

      if (abs < 1 || abs > 3) {
        return false;
      }

      int signum = signum(difference);

      for (int i = 1; i < levels.size() - 1; i++) {
        int nextDifference = levels.get(i + 1) - levels.get(i);
        int nextAbs = abs(nextDifference);

        if (signum != signum(nextDifference) || nextAbs < 1 || nextAbs > 3) {
          return false;
        }
      }

      return true;
    }

  }

}
