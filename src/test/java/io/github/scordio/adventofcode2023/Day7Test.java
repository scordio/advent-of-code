package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.reverseOrder;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 7: Camel Cards")
class Day7Test {

  private static final Pattern PATTERN = compile("(\\w{5}) (\\d+)");

  @ParameterizedTest
  @CsvSource({
    "day7-example1, 6440",
    "day7, 248217452",
  })
  void part1(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      List<Hand> hands = scanner.findAll(PATTERN)
        .map(result -> new Hand(result, false))
        .sorted(reverseOrder())
        .toList();

      long answer = range(0, hands.size())
        .map(rank -> hands.get(rank).getWinnings(rank + 1))
        .sum();

      assertEquals(expected, answer);
    }
  }

  @ParameterizedTest
  @CsvSource({
    "day7-example1, 5905",
    "day7, 245576185",
  })
  void part2(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      List<? extends Hand> hands = scanner.findAll(PATTERN)
        .map(result -> new Hand(result, true))
        .sorted(reverseOrder())
        .toList();

      long answer = range(0, hands.size())
        .map(rank -> hands.get(rank).getWinnings(rank + 1))
        .sum();

      assertEquals(expected, answer);
    }
  }

  private static class Hand implements Comparable<Hand> {

    private static final Comparator<Character> DEFAULT_ORDER = comparingInt("AKQJT98765432"::indexOf);
    private static final Comparator<Character> WITH_JOKER_ORDER = comparingInt("AKQT98765432J"::indexOf);

    private final String cards;
    private final Type type;
    private final int bid;
    private final Comparator<Character> cardComparator;

    private Hand(MatchResult result, boolean withJoker) {
      this.cards = result.group(1);
      this.type = Type.of(cards, withJoker);
      this.bid = parseInt(result.group(2));
      this.cardComparator = withJoker ? WITH_JOKER_ORDER : DEFAULT_ORDER;
    }

    int getWinnings(int rank) {
      return bid * rank;
    }

    @Override
    public int compareTo(Hand other) {
      int typeResult = type.compareTo(other.type);
      return typeResult != 0 ? typeResult : compareCards(cards, other.cards);
    }

    private int compareCards(String cards, String others) {
      return range(0, 5)
        .map(i -> cardComparator.compare(cards.charAt(i), others.charAt(i)))
        .filter(value -> value != 0)
        .findFirst()
        .orElse(0);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Hand.class.getSimpleName() + "[", "]")
        .add("cards='" + cards + "'")
        .add("type=" + type)
        .add("bid=" + bid)
        .toString();
    }

    private enum Type {

      FIVE_OF_A_KIND,
      FOUR_OF_A_KIND,
      FULL_HOUSE,
      THREE_OF_A_KIND,
      TWO_PAIR,
      ONE_PAIR,
      HIGH_CARD;

      private static Type of(String cards, boolean withJoker) {
        long jokers = withJoker ? cards.chars().filter(card -> card == 'J').count() : 0;

        Map<Character, Long> groups = cards.chars()
          .filter(card -> !withJoker || card != 'J')
          .mapToObj(value -> (char) value)
          .collect(groupingBy(identity(), counting()));

        if (groups.values().stream().mapToInt(Long::intValue).max().orElse(0) + jokers == 5) return FIVE_OF_A_KIND;
        if (groups.values().stream().mapToInt(Long::intValue).max().orElseThrow() + jokers == 4) return FOUR_OF_A_KIND;
        if (groups.values().stream().mapToInt(Long::intValue).min().orElseThrow() == 2) return FULL_HOUSE;
        if (groups.values().stream().mapToInt(Long::intValue).max().orElseThrow() + jokers == 3) return THREE_OF_A_KIND;
        if (groups.keySet().size() == 3) return TWO_PAIR;
        if (groups.values().stream().mapToInt(Long::intValue).max().orElseThrow() == 2) return ONE_PAIR;
        if (jokers == 1) return ONE_PAIR;

        return HIGH_CARD;
      }

    }

  }

}
