package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.iterate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 8: Haunted Wasteland")
class Day8Test {

  @ParameterizedTest
  @CsvSource({
    "day8-example1, 2",
    "day8-example2, 6",
    "day8, 20221",
  })
  void part1(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      Instructions instructions = new Instructions(scanner.nextLine());

      scanner.nextLine();

      Map<String, Destination> network = scanner.findAll("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)")
        .collect(toMap(result -> result.group(1), result -> new Destination(result.group(2), result.group(3))));

      long answer = requiredSteps("AAA", node -> node.equals("ZZZ"), instructions, network);

      assertEquals(expected, answer);
    }
  }

  @ParameterizedTest
  @CsvSource({
    "day8-example3, 6",
    "day8, 14616363770447",
  })
  void part2(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      Instructions instructions = new Instructions(scanner.nextLine());

      scanner.nextLine();

      Map<String, Destination> network = scanner.findAll("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)")
        .collect(toMap(result -> result.group(1), result -> new Destination(result.group(2), result.group(3))));

      long answer = network.keySet().stream()
        .filter(key -> key.endsWith("A"))
        .mapToLong(startingNode -> requiredSteps(startingNode, node -> node.endsWith("Z"), instructions, network))
        .mapToObj(BigInteger::valueOf)
        .reduce(Day8Test::lcm)
        .map(BigInteger::longValue)
        .orElseThrow();

      assertEquals(expected, answer);
    }
  }

  private static BigInteger lcm(BigInteger value1, BigInteger value2) {
    BigInteger gcd = value1.gcd(value2);
    BigInteger absProduct = value1.multiply(value2).abs();
    return absProduct.divide(gcd);
  }

  private static long requiredSteps(String startNode, Predicate<String> endPredicate, Instructions instructions, Map<String, Destination> network) {
    return iterate(startNode, endPredicate.negate(), node -> nextNode(node, instructions.getNext(), network)).count();
  }

  private static String nextNode(String node, char instruction, Map<String, Destination> network) {
    Destination destination = network.get(node);
    return instruction == 'L' ? destination.left : destination.right;
  }

  private static class Instructions {

    private final char[] instructions;

    private int current = -1;

    private Instructions(String input) {
      this.instructions = input.toCharArray();
    }

    char getNext() {
      return current == instructions.length - 1 ? instructions[current = 0] : instructions[++current];
    }

  }

  private record Destination(String left, String right) {
  }

}
