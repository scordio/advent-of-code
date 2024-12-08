package io.github.scordio.adventofcode2024;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

import static java.lang.Boolean.TRUE;
import static java.lang.Integer.signum;
import static java.lang.Math.abs;
import static java.util.Arrays.stream;
import static java.util.stream.Gatherers.windowSliding;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 2: Red-Nosed Reports")
class Day2Test {

  @ParameterizedTest
  @CsvSource({
    "day2-example1, 2",
    "day2, 236",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Report::new)
      .filter(Report::isSafe)
      .count();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day2-example1, 4",
    "day2, 308",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .map(Report::new)
      .filter(Report::isSafeWithProblemDumper)
      .count();

    assertEquals(expected, answer);
  }

  private static class Report {

    private final List<Integer> levels;

    Report(String input) {
      this.levels = stream(input.split(" "))
        .map(Integer::valueOf)
        .toList();
    }

    boolean isSafe() {
      return isSafe(levels);
    }

    boolean isSafeWithProblemDumper() {
      return isSafe() || range(0, levels.size())
        .boxed()
        .map(i -> listWithoutIndex(levels, i))
        .anyMatch(Report::isSafe);
    }

    private <T> List<T> listWithoutIndex(List<T> list, int i) {
      return Stream.of(list.subList(0, i), list.subList(i + 1, list.size())).flatMap(List::stream).toList();
    }

    private static boolean isSafe(List<Integer> levels) {
      return levels.stream()
        .gather(windowSliding(2))
        .gather(matchingRules())
        .allMatch(TRUE::equals);
    }

    private static Gatherer<List<Integer>, AtomicInteger, Boolean> matchingRules() {
      return Gatherer.ofSequential(
        AtomicInteger::new,
        (state, pair, downstream) -> {
          int difference = pair.get(1) - pair.get(0);
          int abs = abs(difference);

          if (abs < 1 || abs > 3) {
            return downstream.push(false);
          }

          int signum = signum(difference);

          return state.compareAndSet(0, signum)
            ? downstream.push(true)
            : downstream.push(state.get() == signum);
        });
    }

  }

}
