package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 19: Aplenty")
class Day19Test {

  @ParameterizedTest
  @CsvSource({
    "day19-example1, 19114",
    "day19, 367602",
  })
  void part1(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8).useDelimiter("\\R\\R")) {
      Workflows workflows = parseWorkflows(scanner.next());
      List<Part> parts = parseParts(scanner.next());

      long answer = parts.stream()
        .filter(workflows::accept)
        .mapToLong(part -> part.x() + part.m() + part.a() + part.s())
        .sum();

      assertEquals(expected, answer);
    }
  }

  @Disabled("not implemented")
  @ParameterizedTest
  @CsvSource({
    "day19-example1, 167409079868000",
    "day19, -1",
  })
  void part2(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8).useDelimiter("\\R\\R")) {
      Workflows workflows = parseWorkflows(scanner.next());

      long answer = 0;

      assertEquals(expected, answer);
    }
  }

  private static Workflows parseWorkflows(String input) {
    return new Workflows(input);
  }

  private static List<Part> parseParts(String input) {
    return compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}")
      .matcher(input)
      .results()
      .map(Part::new)
      .toList();
  }

  private static class Workflows {

    private final Map<String, Workflow> workflows;

    private Workflows(String workflows) {
      this.workflows = compile("(\\w+)\\{(.*)}")
        .matcher(workflows)
        .results()
        .map(Workflow::new)
        .collect(toMap(Workflow::getName, identity()));
    }

    boolean accept(Part part) {
      String nextWorkflow = "in";

      while (true) {
        nextWorkflow = workflows.get(nextWorkflow).apply(part);

        if ("A".equals(nextWorkflow)) return true;
        if ("R".equals(nextWorkflow)) return false;
      }
    }

  }

  private static class Workflow implements Function<Part, String> {

    private static final Pattern RULE_PATTERN = compile("(\\w+)([<>]?)(\\d*):?(\\w*)");

    private final String name;
    private final List<Rule> rules;

    Workflow(MatchResult result) {
      this.name = result.group(1);

      this.rules = RULE_PATTERN.matcher(result.group(2))
        .results()
        .map(Rule::new)
        .toList();
    }

    String getName() {
      return name;
    }

    @Override
    public String apply(Part part) {
      return rules.stream()
        .filter(rule -> rule.test(part))
        .map(Rule::getDestination)
        .findFirst()
        .orElseThrow();
    }

  }

  private static class Rule {

    private final String category;
    private final String condition;
    private final int threshold;
    private final String destination;

    private Rule(MatchResult result) {
      if (result.group(2).isEmpty()) {
        this.category = null;
        this.condition = "*";
        this.threshold = 0;
        this.destination = result.group(1);
      } else {
        this.category = result.group(1);
        this.condition = result.group(2);
        this.threshold = parseInt(result.group(3));
        this.destination = result.group(4);
      }
    }

    boolean test(Part part) {
      if (condition.equals("*")) return true;

      int value = switch (category) {
        case "x" -> part.x();
        case "m" -> part.m();
        case "a" -> part.a();
        case "s" -> part.s();
        default -> throw new IllegalStateException("Unexpected value: " + category);
      };

      return condition.equals("<") ? value < threshold : value > threshold;
    }

    String getDestination() {
      return destination;
    }

  }

  private record Part(int x, int m, int a, int s) {

    private Part(MatchResult result) {
      this(parseInt(result.group(1)), parseInt(result.group(2)), parseInt(result.group(3)), parseInt(result.group(4)));
    }

  }

}
