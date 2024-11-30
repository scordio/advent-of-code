package io.github.scordio.adventofcode2023;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 2: Cube Conundrum")
class Day2Test {

  @ParameterizedTest
  @CsvSource({
    "day2-example1, 8",
    "day2, 2149",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Game::new)
      .filter(game -> game.isPossible("12 red, 13 green, 14 blue"))
      .mapToInt(Game::getId)
      .sum();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day2-example1, 2286",
    "day2, 71274",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Game::new)
      .mapToInt(Game::getPower)
      .sum();

    assertEquals(expected, answer);
  }

  private static class Game {

    private final int id;
    private final List<Cubes> revealedCubes;

    private Game(String input) {
      Pattern pattern = compile("Game (\\d+): (.+)");
      Matcher matcher = pattern.matcher(input);
      if (!matcher.find()) throw new IllegalArgumentException("input");

      this.id = parseInt(matcher.group(1));
      this.revealedCubes = parseRevealedCubes(matcher.group(2));
    }

    private static List<Cubes> parseRevealedCubes(String input) {
      return Stream.of(input.split(";")).map(Cubes::new).toList();
    }

    private int getId() {
      return id;
    }

    private boolean isPossible(String input) {
      Cubes expected = new Cubes(input);
      return revealedCubes.stream().allMatch(cubes -> cubes.isPossible(expected));
    }

    private int getPower() {
      return revealedCubes.stream().reduce((left, right) -> new Cubes(
          max(left.red, right.red),
          max(left.green, right.green),
          max(left.blue, right.blue)
        ))
        .orElseThrow()
        .getPower();
    }

  }

  private static class Cubes {

    private static final Pattern PATTERN = compile("(\\d+) (red|green|blue)");

    private final int red;
    private final int green;
    private final int blue;

    private Cubes(String input) {
      Map<String, Integer> cubes = PATTERN.matcher(input)
        .results()
        .collect(toMap(result -> result.group(2), result -> parseInt(result.group(1))));

      this.red = cubes.getOrDefault("red", 0);
      this.green = cubes.getOrDefault("green", 0);
      this.blue = cubes.getOrDefault("blue", 0);
    }

    private Cubes(int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
    }

    private boolean isPossible(Cubes expected) {
      return this.red <= expected.red && this.green <= expected.green && this.blue <= expected.blue;
    }

    private int getPower() {
      return red * green * blue;
    }

  }

}
