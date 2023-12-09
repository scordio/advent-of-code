package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.LongUnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static java.lang.Long.parseLong;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.regex.Pattern.compile;
import static java.util.stream.LongStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 5: If You Give A Seed A Fertilizer")
class Day5Test {

  private static final Pattern SEED_PATTERN = compile("(\\d+)");
  private static final Pattern SEED_RANGE_PATTERN = compile("(\\d+) (\\d+)");
  private static final Pattern MAP_PATTERN = compile("(\\d+) (\\d+) (\\d+)");

  @ParameterizedTest
  @CsvSource({
    "day5-example1, 35",
    "day5, 323142486",
  })
  void part1(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8).useDelimiter("\\R\\R")) {
      long[] seeds = parseSeeds(scanner.next());

      LongUnaryOperator seedToSoil = parseMap(scanner.next());
      LongUnaryOperator soilToFertilizer = parseMap(scanner.next());
      LongUnaryOperator fertilizerToWater = parseMap(scanner.next());
      LongUnaryOperator waterToLight = parseMap(scanner.next());
      LongUnaryOperator lightToTemperature = parseMap(scanner.next());
      LongUnaryOperator temperatureToHumidity = parseMap(scanner.next());
      LongUnaryOperator humidityToLocation = parseMap(scanner.next());

      long answer = Arrays.stream(seeds)
        .map(seedToSoil)
        .map(soilToFertilizer)
        .map(fertilizerToWater)
        .map(waterToLight)
        .map(lightToTemperature)
        .map(temperatureToHumidity)
        .map(humidityToLocation)
        .min()
        .orElseThrow();

      assertEquals(expected, answer);
    }
  }

  private static long[] parseSeeds(String input) {
    return SEED_PATTERN.matcher(input)
      .results()
      .map(MatchResult::group)
      .mapToLong(Long::parseLong)
      .toArray();
  }

  @Disabled("Slow execution")
  @ParameterizedTest
  @CsvSource({
    "day5-example1, 46",
    "day5, 79874951",
  })
  void part2(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8).useDelimiter("\\R\\R")) {
      LongStream seedStream = parseSeedRange(scanner.next());

      LongUnaryOperator seedToSoil = parseMap(scanner.next());
      LongUnaryOperator soilToFertilizer = parseMap(scanner.next());
      LongUnaryOperator fertilizerToWater = parseMap(scanner.next());
      LongUnaryOperator waterToLight = parseMap(scanner.next());
      LongUnaryOperator lightToTemperature = parseMap(scanner.next());
      LongUnaryOperator temperatureToHumidity = parseMap(scanner.next());
      LongUnaryOperator humidityToLocation = parseMap(scanner.next());

      long answer = seedStream.parallel()
        .map(seedToSoil)
        .map(soilToFertilizer)
        .map(fertilizerToWater)
        .map(waterToLight)
        .map(lightToTemperature)
        .map(temperatureToHumidity)
        .map(humidityToLocation)
        .min()
        .orElseThrow();

      assertEquals(expected, answer);
    }
  }

  private static LongStream parseSeedRange(String input) {
    return SEED_RANGE_PATTERN.matcher(input)
      .results()
      .map(Day5Test::processSeedRange)
      .reduce(LongStream::concat)
      .orElseThrow();
  }

  private static LongStream processSeedRange(MatchResult result) {
    long rangeStart = parseLong(result.group(1));
    long rangeLength = parseLong(result.group(2));
    return range(rangeStart, rangeStart + rangeLength);
  }

  private static LongUnaryOperator parseMap(String input) {
    return MAP_PATTERN.matcher(input)
      .results()
      .map(Day5Test::processMapRange)
      .reduce(Day5Test::andThenIfSameAsInput)
      .orElseThrow();
  }

  private static LongUnaryOperator processMapRange(MatchResult result) {
    long destinationRangeStart = parseLong(result.group(1));
    long sourceRangeStart = parseLong(result.group(2));
    long rangeLength = parseLong(result.group(3));

    return operand -> operand >= sourceRangeStart && operand < sourceRangeStart + rangeLength
      ? destinationRangeStart + (operand - sourceRangeStart)
      : operand;
  }

  private static LongUnaryOperator andThenIfSameAsInput(LongUnaryOperator first, LongUnaryOperator second) {
    return value -> {
      long result = first.applyAsLong(value);
      return result == value ? second.applyAsLong(result) : result;
    };
  }

}
