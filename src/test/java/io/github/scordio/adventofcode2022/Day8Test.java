package io.github.scordio.adventofcode2022;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 8: Treetop Tree House")
class Day8Test {

  @ParameterizedTest
  @CsvSource({
    "day8-example, 21",
    "day8, 1832",
  })
  void part1(@ScanInput Scanner scanner, int expected) {
    int[][] trees = scanner.tokens()
      .map(line -> line.chars().map(Character::getNumericValue).toArray())
      .toArray(int[][]::new);

    long answer = range(0, trees.length)
      .mapToLong(i -> range(0, trees[i].length).filter(j -> isVisible(trees, i, j)).count())
      .sum();

    assertEquals(expected, answer);
  }

  private static boolean isVisible(int[][] trees, int i, int j) {
    return stream(trees[i], 0, j).allMatch(tree -> tree < trees[i][j])
           || stream(trees[i], j + 1, trees[0].length).allMatch(tree -> tree < trees[i][j])
           || stream(trees, 0, i).mapToInt(treeLine -> treeLine[j]).allMatch(tree -> tree < trees[i][j])
           || stream(trees, i + 1, trees.length).mapToInt(treeLine -> treeLine[j]).allMatch(tree -> tree < trees[i][j]);
  }

  @ParameterizedTest
  @CsvSource({
    "day8-example, 8",
    "day8, 157320",
  })
  void part2(@ScanInput Scanner scanner, int expected) {
    int[][] trees = scanner.tokens()
      .map(line -> line.chars().map(Character::getNumericValue).toArray())
      .toArray(int[][]::new);

    long answer = range(1, trees.length - 1)
      .mapToLong(i -> range(1, trees[i].length - 1).mapToLong(j -> scenicScore(trees, i, j)).max().orElseThrow())
      .max().orElseThrow();

    assertEquals(expected, answer);
  }

  private static long scenicScore(int[][] trees, int i, int j) {
    return (reverseStream(trees[i], 0, j).takeWhile(inclusive(tree -> tree < trees[i][j])).count())
           * (stream(trees[i], j + 1, trees[0].length).takeWhile(inclusive(tree -> tree < trees[i][j])).count())
           * (reverseStream(trees, 0, i).mapToInt(treeLine -> treeLine[j]).takeWhile(inclusive(tree -> tree < trees[i][j])).count())
           * (stream(trees, i + 1, trees.length).mapToInt(treeLine -> treeLine[j]).takeWhile(inclusive(tree -> tree < trees[i][j])).count());
  }

  private static IntStream reverseStream(int[] array, int startInclusive, int endExclusive) {
    return range(0, endExclusive - startInclusive).map(i -> array[endExclusive - i - 1]);
  }

  private static <T> Stream<T> reverseStream(T[] array, int startInclusive, int endExclusive) {
    return range(0, endExclusive - startInclusive).mapToObj(i -> array[endExclusive - i - 1]);
  }

  private static IntPredicate inclusive(IntPredicate delegate) {
    AtomicBoolean takeNext = new AtomicBoolean(true);
    return value -> delegate.test(value) ? takeNext.get() : takeNext.getAndSet(false);
  }

}
