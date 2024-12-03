package io.github.scordio.adventofcode2024;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.function.ToIntFunction;
import java.util.regex.MatchResult;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 3: Mull It Over")
class Day3Test {

  private static final String MULTIPLY_PATTERN = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
  private static final String INSTRUCTIONS_PATTERN = MULTIPLY_PATTERN + "|do\\(\\)|don't\\(\\)";

  @ParameterizedTest
  @CsvSource({
    "day3-example1, 161",
    "day3, 164730528",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.findAll(MULTIPLY_PATTERN)
      .mapToInt(Day3Test::multiply)
      .sum();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day3-example2, 48",
    "day3, 70478672",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.findAll(INSTRUCTIONS_PATTERN)
      .mapMultiToInt(ifEnabled(Day3Test::multiply))
      .sum();

    assertEquals(expected, answer);
  }

  private static BiConsumer<MatchResult, IntConsumer> ifEnabled(ToIntFunction<MatchResult> delegate) {
    AtomicBoolean enabled = new AtomicBoolean(true);
    return (result, consumer) -> {
      if (result.group().equals("do()")) {
        enabled.set(true);
      } else if (result.group().equals("don't()")) {
        enabled.set(false);
      } else if (enabled.get()) {
        consumer.accept(delegate.applyAsInt(result));
      }
    };
  }

  private static int multiply(MatchResult result) {
    return parseInt(result.group(1)) * parseInt(result.group(2));
  }

}
