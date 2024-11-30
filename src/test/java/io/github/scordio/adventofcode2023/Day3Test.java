package io.github.scordio.adventofcode2023;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Integer.compare;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 3: Gear Ratios")
class Day3Test {

  private static final Pattern PATTERN = compile("(\\d+|[^.\n])");

  @ParameterizedTest
  @CsvSource({
    "day3-example1, 11, 4361",
    "day3, 141, 517021",
  })
  void part1(@ScanInput Scanner scanner, int lineLength, int expected) {
    // workaround to force scanner buffer increase - see Scanner.BUFFER_SIZE
    scanner.useDelimiter("(?s).*").hasNext();

    Map<Position, Element> elements = scanner.findAll(PATTERN)
      .map(result -> new Element(
        new Position(result.start() / lineLength, result.start() % lineLength),
        result.group()))
      .collect(toMap(Element::getPosition, identity()));

    long answer = elements.values().stream()
      .filter(element -> isPartNumber(element, elements))
      .mapToInt(Element::getNumber)
      .sum();

    assertEquals(expected, answer);
  }

  private static boolean isPartNumber(Element element, Map<Position, Element> elements) {
    if (element.isSymbol()) return false;

    return element.getAdjacentPositions().stream()
      .map(elements::get)
      .filter(Objects::nonNull)
      .anyMatch(Element::isSymbol);
  }

  @ParameterizedTest
  @CsvSource({
    "day3-example1, 11, 467835",
    "day3-example2, 10, 276168",
    "day3, 141, 81296995",
  })
  void part2(@ScanInput Scanner scanner, int lineLength, int expected) {
    // workaround to force scanner buffer increase - see Scanner.BUFFER_SIZE
    scanner.useDelimiter("(?s).*").hasNext();

    Map<Position, Element> elements = scanner.findAll(PATTERN)
      .map(result -> new Element(
        new Position(result.start() / lineLength, result.start() % lineLength),
        result.group()))
      .collect(toMap(Element::getPosition, identity()));

    long answer = elements.values().stream()
      .mapToInt(element -> getGearRatio(element, elements))
      .sum();

    assertEquals(expected, answer);
  }

  private static int getGearRatio(Element element, Map<Position, Element> elements) {
    if (element.isNumber() || element.isNotGear()) return 0;

    List<Integer> parts = getAdjacentNumbers(element, elements);

    return parts.size() == 2
      ? parts.getFirst() * parts.getLast()
      : 0;
  }

  private static List<Integer> getAdjacentNumbers(Element element, Map<Position, Element> elements) {
    Position position = element.getPosition();

    return Stream.concat(
        element.getAdjacentPositions().stream(),
        Stream.of(
          new Position(position.row - 1, position.column - 3),
          new Position(position.row - 1, position.column - 2),
          new Position(position.row, position.column - 3),
          new Position(position.row, position.column - 2),
          new Position(position.row + 1, position.column - 3),
          new Position(position.row + 1, position.column - 2)))
      .map(elements::get)
      .filter(Objects::nonNull)
      .filter(Element::isNumber)
      .filter(e -> position.column() - e.getPosition().column <= Integer.toString(e.getNumber()).length())
      .map(Element::getNumber)
      .toList();
  }

  private static class Element {

    private final Position position;
    private final Integer number;
    private final Character symbol;

    private Element(Position position, String input) {
      this.position = position;

      OptionalInt value = parseInt(input);

      if (value.isPresent()) {
        this.number = value.getAsInt();
        this.symbol = null;
      } else {
        if (input.length() != 1) throw new IllegalArgumentException(input);
        this.symbol = input.charAt(0);
        this.number = null;
      }
    }

    private static OptionalInt parseInt(String input) {
      try {
        return OptionalInt.of(Integer.parseInt(input));
      } catch (NumberFormatException e) {
        return OptionalInt.empty();
      }
    }

    private Position getPosition() {
      return position;
    }

    private Set<Position> getAdjacentPositions() {
      int length = number != null ? number.toString().length() : 1;

      return Stream.of(
          iterateAlongRow(position.row - 1, position.column, length),
          Stream.of(
            new Position(position.row, position.column - 1),
            new Position(position.row, position.column + length)),
          iterateAlongRow(position.row + 1, position.column, length))
        .flatMap(identity())
        .collect(toSet());
    }

    private static Stream<Position> iterateAlongRow(int row, int column, int elementLength) {
      return Stream.iterate(
        new Position(row, column - 1),
        position -> position.column < column + elementLength + 1,
        position -> new Position(row, position.column + 1));
    }

    private boolean isNumber() {
      return number != null;
    }

    private boolean isSymbol() {
      return symbol != null;
    }

    private boolean isNotGear() {
      return symbol != '*';
    }

    private int getNumber() {
      return Objects.requireNonNull(number);
    }

    @Override
    public String toString() {
      return number != null ? number.toString() : symbol.toString();
    }
  }

  private record Position(int row, int column) implements Comparable<Position> {

    @Override
    public int compareTo(Position other) {
      return this.row == other.row ? compare(this.column, other.column) : compare(this.row, other.row);
    }

  }

}
