package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day 1: Calorie Counting")
class Day1Test {

  @Test
  void part1() throws Exception {
    try (var inputStream = getClass().getResourceAsStream("day1")) {
      int answer = stream(new String(inputStream.readAllBytes(), UTF_8).split("\\n\\n"))
        .mapToInt(block -> block.lines()
          .mapToInt(Integer::parseInt)
          .sum())
        .max()
        .orElseThrow();

      assertThat(answer).isEqualTo(72511);
    }
  }

  @Test
  void part2() throws Exception {
    try (var inputStream = getClass().getResourceAsStream("day1")) {
      int answer = stream(new String(inputStream.readAllBytes(), UTF_8).split("\\n\\n"))
        .mapToInt(block -> block.lines()
          .mapToInt(Integer::parseInt)
          .sum())
        .map(i -> ~i)
        .sorted()
        .map(i -> ~i)
        .limit(3)
        .sum();

      assertThat(answer).isEqualTo(212117);
    }
  }

}
