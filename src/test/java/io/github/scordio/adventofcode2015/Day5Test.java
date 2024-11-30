package io.github.scordio.adventofcode2015;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;
import java.util.function.Predicate;

import static java.util.regex.Pattern.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 5: Doesn't He Have Intern-Elves For This?")
class Day5Test {

  private static final Predicate<String> HAS_THREE_VOWELS = compile(".*[aeiou].*[aeiou].*[aeiou].*").asMatchPredicate();
  private static final Predicate<String> HAS_DOUBLE_LETTER = compile(".*(.)\\1+.*").asMatchPredicate();
  private static final Predicate<String> DOES_NOT_HAVE_FORBIDDEN_STRINGS = compile(".*(ab|cd|pq|xy).*").asMatchPredicate().negate();

  @ParameterizedTest
  @CsvSource({
    "day5-example1, 1",
    "day5-example2, 1",
    "day5-example3, 0",
    "day5-example4, 0",
    "day5-example5, 0",
    "day5, 238",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .filter(HAS_THREE_VOWELS.and(HAS_DOUBLE_LETTER).and(DOES_NOT_HAVE_FORBIDDEN_STRINGS))
      .count();

    assertEquals(expected, answer);
  }

  private static final Predicate<String> HAS_PAIR_TWICE_WITHOUT_OVERLAPPING = compile(".*(..).*\\1.*").asMatchPredicate();
  private static final Predicate<String> HAS_LETTER_TWICE_WITH_ONE_IN_BETWEEN = compile(".*(.).\\1+.*").asMatchPredicate();

  @ParameterizedTest
  @CsvSource({
    "day5-example6, 1",
    "day5-example7, 1",
    "day5-example8, 0",
    "day5-example9, 0",
    "day5, 69",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    long answer = scanner.tokens()
      .filter(HAS_PAIR_TWICE_WITHOUT_OVERLAPPING.and(HAS_LETTER_TWICE_WITH_ONE_IN_BETWEEN))
      .count();

    assertEquals(expected, answer);
  }

}
