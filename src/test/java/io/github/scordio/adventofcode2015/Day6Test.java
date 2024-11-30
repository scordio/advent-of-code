package io.github.scordio.adventofcode2015;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 6: Probably a Fire Hazard")
class Day6Test {

  @ParameterizedTest
  @CsvSource({
    "day6-example1, 1_000_000",
    "day6-example2, 1000",
    "day6-example3, 999_996",
    "day6, 377891",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    int[][] lights = new int[1000][1000];

    scanner.tokens()
      .map(Command::new)
      .forEach(command -> command.power(lights));

    long answer = Stream.of(lights)
      .flatMapToInt(Arrays::stream)
      .sum();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day6-example4, 1",
    "day6-example5, 2_000_000",
    "day6, 14110788",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    int[][] lights = new int[1000][1000];

    scanner.tokens()
      .map(Command::new)
      .forEach(command -> command.brightness(lights));

    long answer = Stream.of(lights)
      .flatMapToInt(Arrays::stream)
      .sum();

    assertEquals(expected, answer);
  }

  private static class Command {

    private final Action action;
    private final Coordinates from;
    private final Coordinates to;

    private Command(String command) {
      String[] parts = command.split(" (?=\\d|through)|,");
      this.action = Action.valueOf(parts[0].replace(" ", "_").toUpperCase());
      this.from = new Coordinates(parseInt(parts[1]), parseInt(parts[2]));
      this.to = new Coordinates(parseInt(parts[4]), parseInt(parts[5]));
    }

    private void power(int[][] lights) {
      for (int x = from.x; x <= to.x; ++x) {
        for (int y = from.y; y <= to.y; ++y) {
          lights[x][y] = switch (action) {
            case TURN_ON -> 1;
            case TURN_OFF -> 0;
            case TOGGLE -> 1 - lights[x][y];
          };
        }
      }
    }

    private void brightness(int[][] lights) {
      for (int x = from.x; x <= to.x; ++x) {
        for (int y = from.y; y <= to.y; ++y) {
          lights[x][y] = switch (action) {
            case TURN_ON -> lights[x][y] + 1;
            case TURN_OFF -> lights[x][y] > 0 ? --lights[x][y] : 0;
            case TOGGLE -> lights[x][y] + 2;
          };
        }
      }
    }

    private enum Action {

      TURN_ON,
      TURN_OFF,
      TOGGLE

    }

    private record Coordinates(int x, int y) {
    }

  }

}
