package io.github.scordio.adventofcode2024;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.regex.Pattern.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 5: Print Queue")
class Day5Test {

  private static final Pattern ORDERING_RULES_PATTERN = compile("(\\d\\d)\\|(\\d\\d)");

  @ParameterizedTest
  @CsvSource({
    "day5-example1, 143",
    "day5, 5275",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    Comparator<Integer> orderingRules = scanner.findAll(ORDERING_RULES_PATTERN)
      .map(Day5Test::parseRule)
      .reduce(Comparator::thenComparing)
      .orElseThrow();

    scanner.next();

    int answer = scanner.tokens()
      .map(Update::new)
      .filter(update -> update.isSorted(orderingRules))
      .mapToInt(Update::getMiddlePage)
      .sum();

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "day5-example1, 123",
    "day5, 6191",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    Comparator<Integer> orderingRules = scanner.findAll(ORDERING_RULES_PATTERN)
      .map(Day5Test::parseRule)
      .reduce(Comparator::thenComparing)
      .orElseThrow();

    scanner.next();

    int answer = scanner.tokens()
      .map(Update::new)
      .filter(update -> !update.isSorted(orderingRules))
      .mapToInt(update -> update.getMiddlePage(orderingRules))
      .sum();

    assertEquals(expected, answer);
  }

  private static Comparator<Integer> parseRule(MatchResult result) {
    int page1 = parseInt(result.group(1));
    int page2 = parseInt(result.group(2));
    return (i1, i2) -> i1 == page1 && i2 == page2 ? -1 : i1 == page2 && i2 == page1 ? 1 : 0;
  }

  private static class Update {

    private final List<Integer> pages;

    Update(String input) {
      this.pages = stream(input.split(",")).map(Integer::valueOf).toList();
    }

    boolean isSorted(Comparator<Integer> orderingRules) {
      List<Integer> sorted = pages.stream().sorted(orderingRules).toList();
      return pages.equals(sorted);
    }

    int getMiddlePage() {
      return pages.get(pages.size() / 2);
    }

    int getMiddlePage(Comparator<Integer> orderingRules) {
      return pages.stream().sorted(orderingRules).toList().get(pages.size() / 2);
    }

  }

}
