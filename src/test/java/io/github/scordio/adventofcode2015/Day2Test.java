package io.github.scordio.adventofcode2015;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 2: I Was Told There Would Be No Math")
class Day2Test {

  @ParameterizedTest
  @CsvSource({
    "day2-example1, 58",
    "day2-example2, 43",
    "day2, 1598415",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Box::new)
      .mapToInt(Box::getRequiredPaperFeet)
      .sum();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day2-example1, 34",
    "day2-example2, 14",
    "day2, 3812909",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Box::new)
      .mapToInt(Box::getRequiredRibbonFeet)
      .sum();

    assertEquals(expected, answer);
  }

  static class Box {

    private final int length;
    private final int width;
    private final int height;

    Box(String value) {
      String[] parts = value.split("x");
      this.length = Integer.parseInt(parts[0]);
      this.width = Integer.parseInt(parts[1]);
      this.height = Integer.parseInt(parts[2]);
    }

    private int getSurfaceArea() {
      return 2 * length * width + 2 * width * height + 2 * height * length;
    }

    private int getSmallestSideArea() {
      return IntStream.of(length * width, width * height, height * length)
        .sorted()
        .limit(1)
        .findAny()
        .orElseThrow();
    }

    int getRequiredPaperFeet() {
      return getSurfaceArea() + getSmallestSideArea();
    }

    private int getWrapRibbonFeet() {
      return IntStream.of(length, width, height)
        .sorted()
        .limit(2)
        .map(value -> 2 * value)
        .sum();
    }

    private int getBowRibbonFeet() {
      return length * width * height;
    }

    int getRequiredRibbonFeet() {
      return getWrapRibbonFeet() + getBowRibbonFeet();
    }

  }

}
