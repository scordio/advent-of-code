package io.github.scordio.adventofcode2023;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.MatchResult;

import static java.lang.Long.parseLong;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 24: Never Tell Me The Odds")
class Day24Test {

  @ParameterizedTest
  @CsvSource({
    "day24-example1, 7, 27, 2",
    "day24, 200000000000000, 400000000000000, 16939",
  })
  void part1(@ScanInput Scanner scanner, long min, long max, long expected) {
    List<Line2D> paths = scanner.findAll("(\\d+), (\\d+), (\\d+) @\\s+(-?\\d+),\\s+(-?\\d+),\\s(-?\\d+)")
      .map(Hailstone::new)
      .map(hailstone -> hailstone.getPath(min, max))
      .filter(Objects::nonNull)
      .toList();

    long answer = range(0, paths.size())
      .flatMap(i -> range(i + 1, paths.size()).filter(j -> paths.get(i).intersectsLine(paths.get(j))))
      .count();

    assertEquals(expected, answer);
  }

  @Disabled("not implemented")
  @ParameterizedTest
  @CsvSource({
    "day24-example1, 47",
    "day24, -1",
  })
  void part2(@ScanInput Scanner scanner, long expected) {
    long answer = 0;

    assertEquals(expected, answer);
  }

  private static class Hailstone {

    private final long px;
    private final long py;
    private final long vx;
    private final long vy;

    private Hailstone(MatchResult result) {
      this.px = parseLong(result.group(1));
      this.py = parseLong(result.group(2));
      this.vx = parseLong(result.group(4));
      this.vy = parseLong(result.group(5));
    }

    Line2D getPath(long min, long max) {
      Point2D start = new Point2D.Double(px, py);

      if (isOutside(start, min, max)) start = calculateStart(min, max);
      if (isOutside(start, min, max)) return null;

      Point2D end = calculateEnd(start, min, max);

      return new Line2D.Double(start, end);
    }

    private static boolean isOutside(Point2D point, long min, long max) {
      return !(point.getY() >= min &&
               point.getY() >= min &&
               point.getX() <= max &&
               point.getY() <= max);
    }

    private Point2D calculateStart(long min, long max) {
      double dx = px < min
        ? (double) (min - px) / vx
        : px > max ? (double) (max - px) / vx : 0;

      double dy = py < min
        ? (double) (min - py) / vy
        : py > max ? (double) (max - py) / vy : 0;

      double distance = max(dx, dy);

      return new Point2D.Double(distance * vx + px, distance * vy + py);
    }

    private Point2D calculateEnd(Point2D start, long min, long max) {
      double dx = vx > 0
        ? (max - start.getX()) / vx
        : (min - start.getX()) / vx;

      double dy = vy > 0
        ? (max - start.getY()) / vy
        : (min - start.getY()) / vy;

      double distance = min(dx, dy);

      return new Point2D.Double(distance * vx + start.getX(), distance * vy + start.getY());
    }

  }

}
