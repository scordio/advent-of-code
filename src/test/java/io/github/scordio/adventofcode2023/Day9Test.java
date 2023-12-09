package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.regex.Pattern.compile;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.iterate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 9: Mirage Maintenance")
class Day9Test {

  @ParameterizedTest
  @CsvSource({
    "day9-example1, 114",
    "day9, 2175229206",
  })
  void part1(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8).useDelimiter("\\R")) {
      long answer = scanner.tokens()
        .map(History::new)
        .mapToLong(History::predictNextValue)
        .sum();

      assertEquals(expected, answer);
    }
  }

  @ParameterizedTest
  @CsvSource({
    "day9-example1, 2",
    "day9, 942",
  })
  void part2(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8).useDelimiter("\\R")) {
      long answer = scanner.tokens()
        .map(History::new)
        .mapToLong(History::predictPreviousValue)
        .sum();

      assertEquals(expected, answer);
    }
  }

  private static class History {

    private static final Pattern PATTERN = compile("(-?\\d+)");

    private final long[] startingSequence;

    private History(String input) {
      this.startingSequence = PATTERN.matcher(input)
        .results()
        .map(MatchResult::group)
        .mapToLong(Long::parseLong)
        .toArray();
    }

    private long predictNextValue() {
      return iterate(startingSequence, History::hasAnyNonZero, History::getNextSequence)
        .map(sequence -> sequence[sequence.length - 1])
        .toList()
        .reversed()
        .stream()
        .reduce(0L, Long::sum);
    }

    private long predictPreviousValue() {
      return iterate(startingSequence, History::hasAnyNonZero, History::getNextSequence)
        .map(sequence -> sequence[0])
        .toList()
        .reversed()
        .stream()
        .reduce(0L, (value1, value2) -> value2 - value1);
    }

    private static boolean hasAnyNonZero(long[] sequence) {
      return stream(sequence).anyMatch(value -> value != 0);
    }

    private static long[] getNextSequence(long[] sequence) {
      return range(0, sequence.length - 1)
        .mapToLong(i -> sequence[i + 1] - sequence[i])
        .toArray();
    }

  }

}
