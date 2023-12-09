package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.LongStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("Day 6: Wait For It")
class Day6Test {

  static Stream<Arguments> part1() {
    return Stream.of(
      arguments(288, List.of(new Race(7, 9), new Race(15, 40), new Race(30, 200))),
      arguments(138915, List.of(new Race(46, 358), new Race(68, 1054), new Race(98, 1807), new Race(66, 1080))));
  }

  @ParameterizedTest
  @MethodSource
  void part1(int expected, List<Race> races) {
    long answer = races.stream()
      .mapToLong(Day6Test::calculateWinningWays)
      .reduce((left, right) -> left * right)
      .orElseThrow();

    assertEquals(expected, answer);
  }

  static Stream<Arguments> part2() {
    return Stream.of(
      arguments(71503, new Race(71530, 940200)),
      arguments(27340847, new Race(46689866, 358105418071080L)));
  }

  @ParameterizedTest
  @MethodSource
  void part2(int expected, Race race) {
    long answer = calculateWinningWays(race);

    assertEquals(expected, answer);
  }

  private static long calculateDistance(long speed, long time) {
    return (time - speed) * speed;
  }

  private static long calculateWinningWays(Race race) {
    return range(1, race.time())
      .map(speed -> calculateDistance(speed, race.time()))
      .filter(distance -> distance > race.distance())
      .count();
  }


  private record Race(long time, long distance) {
  }

}
