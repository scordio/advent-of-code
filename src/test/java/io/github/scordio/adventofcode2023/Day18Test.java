package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.Point;
import java.awt.Polygon;
import java.util.Scanner;
import java.util.regex.MatchResult;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 18: Lavaduct Lagoon")
class Day18Test {

  @ParameterizedTest
  @CsvSource({
    "day18-example1, 62",
    "day18, 36679",
  })
  void part1(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      Lagoon lagoon = new Lagoon();

      scanner.findAll("(\\w) (\\d+) \\((#\\w+)\\)")
        .map(Instruction::new)
        .forEach(lagoon::dig);

      long answer = lagoon.getArea();

      assertEquals(expected, answer);
    }
  }

  @ParameterizedTest
  @CsvSource({
    "day18-example1, 952408144115",
    "day18, 88007104020978",
  })
  void part2(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8).useDelimiter("\\R")) {
      Lagoon lagoon = new Lagoon();

      scanner.findAll("(\\w) (\\d+) \\((#\\w+)\\)")
        .map(Instruction::fromHex)
        .forEach(lagoon::dig);

      long answer = lagoon.getArea();

      assertEquals(expected, answer);
    }
  }

  private static class Lagoon {

    private final Polygon polygon = new Polygon();
    private final Point currentPoint = new Point();

    private long perimeter;

    private Lagoon() {
      polygon.addPoint(currentPoint.x, currentPoint.y);
    }

    private void dig(Instruction instruction) {
      perimeter += instruction.meters();

      switch (instruction.direction()) {
        case "U" -> currentPoint.translate(0, instruction.meters());
        case "D" -> currentPoint.translate(0, -instruction.meters());
        case "L" -> currentPoint.translate(-instruction.meters(), 0);
        case "R" -> currentPoint.translate(instruction.meters(), 0);
      }

      polygon.addPoint(currentPoint.x, currentPoint.y);
    }

    private long getArea() {
      return innerArea(polygon) + perimeter / 2 + 1;
    }

    private static long innerArea(Polygon polygon) {
      long sum = range(0, polygon.npoints)
        .mapToLong(i -> {
          long x1 = polygon.xpoints[i];
          long y1 = polygon.ypoints[i];
          long x2 = polygon.xpoints[i + 1];
          long y2 = polygon.ypoints[i + 1];
          return x1 * y2 - y1 * x2;
        })
        .sum();

      return Math.abs(sum / 2);
    }

  }

  private record Instruction(String direction, int meters) {

    private Instruction(MatchResult result) {
      this(result.group(1), parseInt(result.group(2)));
    }

    private static Instruction fromHex(MatchResult result) {
      String hex = result.group(3);

      int meters = parseInt(hex.substring(1, 6), 16);

      String direction = switch (hex.charAt(6)) {
        case '0' -> "R";
        case '1' -> "D";
        case '2' -> "L";
        case '3' -> "U";
        default -> throw new IllegalStateException("Unexpected value: " + hex.charAt(6));
      };

      return new Instruction(direction, meters);
    }

  }

}
