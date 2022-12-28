package io.github.scordio.adventofcode2022;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Day 1: Calorie Counting")
class Day1Test {

  @Test
  void part1() throws Exception {
    try (InputStream is = this.getClass().getResourceAsStream("day1")) {
      int answer = stream(new String(is.readAllBytes(), UTF_8).split("\\n\\n"))
        .mapToInt(lines -> stream(lines.split("\\n"))
          .mapToInt(Integer::parseInt)
          .sum())
        .max()
        .orElseThrow();

      assertThat(answer).isEqualTo(72511);
    }
  }

  @Test
  void part2() throws Exception {
    try (InputStream is = this.getClass().getResourceAsStream("day1")) {
      int answer = stream(new String(is.readAllBytes(), UTF_8).split("\\n\\n"))
        .mapToInt(lines -> stream(lines.split("\\n"))
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
