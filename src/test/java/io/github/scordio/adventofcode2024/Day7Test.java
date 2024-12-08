package io.github.scordio.adventofcode2024;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.regex.Pattern.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 7: Bridge Repair")
class Day7Test {

  private static final Pattern EQUATION_PATTERN = compile("(\\d+): (.+)");

  @ParameterizedTest
  @CsvSource({
    "day7-example1, 3749",
    "day7, 28730327770375",
  })
  void part1(@ScanInput Scanner scanner, long expected) {
    long answer = scanner.findAll(EQUATION_PATTERN)
      .map(Equation::new)
      .filter(Equation::isTrue)
      .mapToLong(Equation::getTestValue)
      .sum();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day7-example1, 11387",
    "day7, 424977609625985",
  })
  void part2(@ScanInput Scanner scanner, long expected) {
    long answer = scanner.findAll(EQUATION_PATTERN)
      .map(Equation::new)
      .filter(Equation::isTrueWithConcatenation)
      .mapToLong(Equation::getTestValue)
      .sum();

    assertEquals(expected, answer);
  }

  private static class Equation {

    private final long testValue;
    private final List<Long> numbers;

    Equation(MatchResult result) {
      this.testValue = parseLong(result.group(1));
      this.numbers = stream(result.group(2).split(" "))
        .map(Long::valueOf)
        .toList();
    }

    long getTestValue() {
      return testValue;
    }

    boolean isTrue() {
      return isTrue(testValue, numbers, value -> List.of(sum(value), multiply(value)));
    }

    boolean isTrueWithConcatenation() {
      return isTrue(testValue, numbers, value -> List.of(sum(value), multiply(value), concatenate(value)));
    }

    private static boolean isTrue(long testValue, List<Long> numbers, Function<Long, List<LongUnaryOperator>> operationsFactory) {
      List<LongUnaryOperator> operations = numbers.stream()
        .map(operationsFactory)
        .reduce(Equation::cartesianProduct)
        .orElseThrow();

      return operations.stream()
        .mapToLong(operation -> operation.applyAsLong(0L))
        .anyMatch(value -> value == testValue);
    }

    private static List<LongUnaryOperator> cartesianProduct(List<LongUnaryOperator> ops1, List<LongUnaryOperator> ops2) {
      return ops1.stream().flatMap(op -> ops2.stream()
        .map(op::andThen))
        .toList();
    }

    private static LongUnaryOperator sum(long value) {
      return i -> i + value;
    }

    private static LongUnaryOperator multiply(long value) {
      return i -> i * value ;
    }

    private static LongUnaryOperator concatenate(long value) {
      return i -> parseLong(i + "" + value);
    }

  }

}
