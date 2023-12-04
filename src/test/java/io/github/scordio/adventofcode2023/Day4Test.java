package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Stream.iterate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 4: Scratchcards")
class Day4Test {

  @ParameterizedTest
  @CsvSource({
    "day4-example1, 13",
    "day4, 26914",
  })
  void part1(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      long answer = scanner.useDelimiter("\\R").tokens()
        .map(Card::new)
        .mapToInt(Card::getCardValue)
        .sum();

      assertEquals(expected, answer);
    }
  }

  @ParameterizedTest
  @CsvSource({
    "day4-example1, 30",
    "day4, 13080971",
  })
  void part2(String input, int expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      List<Card> cards = scanner.useDelimiter("\\R").tokens()
        .map(Card::new)
        .toList();

      long answer = cards.stream()
        .flatMap(card -> expandCopies(card, cards))
        .count();

      assertEquals(expected, answer);
    }
  }

  private static Stream<Card> expandCopies(Card card, List<Card> cards) {
    return Stream.concat(
      Stream.of(card),
      iterate(card.getCardNumber(), i -> i < cards.size() && i < card.getCardNumber() + card.getWinnersCount(), i -> i + 1)
        .map(cards::get)
        .flatMap(copy -> expandCopies(copy, cards)));
  }

  private static class Card {

    private static final Pattern CARD_PATTERN = compile("Card\\s+(\\d+):(.*)\\|(.*)");
    private static final Pattern NUMBER_PATTERN = compile("(\\d+)");

    private final int cardNumber;
    private final Set<Integer> winningNumbers;
    private final Set<Integer> numbers;


    private Card(String input) {
      Matcher matcher = CARD_PATTERN.matcher(input);

      if (!matcher.find()) throw new IllegalArgumentException(input);

      this.cardNumber = parseInt(matcher.group(1));
      this.winningNumbers = parseNumbers(matcher.group(2));
      this.numbers = parseNumbers(matcher.group(3));
    }

    private static Set<Integer> parseNumbers(String input) {
      return NUMBER_PATTERN.matcher(input)
        .results()
        .map(MatchResult::group)
        .map(Integer::parseInt)
        .collect(toCollection(LinkedHashSet::new));
    }

    public int getCardNumber() {
      return cardNumber;
    }

    private int getCardValue() {
      long winnersCount = getWinnersCount();
      return winnersCount != 0 ? (int) Math.pow(2, winnersCount - 1) : 0;
    }

    private long getWinnersCount() {
      return numbers.stream().filter(winningNumbers::contains).count();
    }

  }

}
