package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day 2: Rock Paper Scissors")
class Day2Test {

  @Test
  void part1() throws Exception {
    var scores = Map.of(
      "A X", 4,
      "A Y", 8,
      "A Z", 3,
      "B X", 1,
      "B Y", 5,
      "B Z", 9,
      "C X", 7,
      "C Y", 2,
      "C Z", 6
    );

    try (var lines = lines(of(getClass().getResource("day2").toURI()))) {
      int answer = lines.mapToInt(scores::get).sum();

      assertThat(answer).isEqualTo(15572);
    }
  }

  @Test
  void part2() throws Exception {
    var scores = Map.of(
      "A X", 3,
      "A Y", 4,
      "A Z", 8,
      "B X", 1,
      "B Y", 5,
      "B Z", 9,
      "C X", 2,
      "C Y", 6,
      "C Z", 7
    );

    try (var lines = lines(of(getClass().getResource("day2").toURI()))) {
      int answer = lines.mapToInt(scores::get).sum();

      assertThat(answer).isEqualTo(16098);
    }
  }

}
