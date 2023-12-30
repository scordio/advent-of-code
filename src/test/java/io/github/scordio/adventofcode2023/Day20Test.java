package io.github.scordio.adventofcode2023;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.SequencedCollection;
import java.util.function.Supplier;
import java.util.regex.MatchResult;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 20: Pulse Propagation")
class Day20Test {

  @ParameterizedTest
  @CsvSource({
    "day20-example1, 32000000",
    "day20-example2, 11687500",
    "day20, 898731036",
  })
  void part1(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      Modules modules = new Modules();

      scanner.findAll("(.+) -> (.+)").forEach(modules::addModule);

      range(0, 1000).forEach(__ -> modules.pushButton());

      long answer = modules.getLowPulsesCount() * modules.getHighPulsesCount();

      assertEquals(expected, answer);
    }
  }

  @Disabled("not implemented")
  @ParameterizedTest
  @CsvSource({
    "day20, -1",
  })
  void part2(String input, long expected) {
    try (var scanner = new Scanner(getClass().getResourceAsStream(input), UTF_8)) {
      Modules modules = new Modules();

      scanner.findAll("(.+) -> (.+)").forEach(modules::addModule);

      long answer = 0;

      assertEquals(expected, answer);
    }
  }

  private static class Modules {

    private final Map<String, Module> modules = new HashMap<>();
    private final SequencedCollection<Runnable> pulses = new LinkedList<>();

    private long lowPulsesCount;
    private long highPulsesCount;

    void addModule(MatchResult result) {
      String name = result.group(1);
      String destinations = result.group(2);

      Module module = switch (name) {
        case "broadcaster" -> new Broadcaster(this, destinations);
        case String s when s.charAt(0) == '%' -> new FlipFlop(this, s.substring(1), destinations);
        case String s when s.charAt(0) == '&' -> new Conjunction(this, s.substring(1), destinations);
        default -> throw new IllegalStateException("Unexpected value: " + name);
      };

      modules.put(module.name(), module);
    }

    void pushButton() {
      ++lowPulsesCount;

      modules.get("broadcaster").receive(null, Pulse.LOW);

      while (!pulses.isEmpty()) {
        Runnable pulse = pulses.removeFirst();
        pulse.run();
      }
    }

    void sendPulse(Module source, Pulse pulse, List<String> destinations) {
      pulses.add(() -> {
        if (pulse == Pulse.HIGH) highPulsesCount += destinations.size();
        else lowPulsesCount += destinations.size();

        destinations.stream()
          .map(modules::get)
          .filter(Objects::nonNull)
          .forEach(module -> module.receive(source, pulse));
      });
    }

    List<Module> getConnectedModules(String name) {
      return modules.values().stream()
        .filter(module -> module.isConnectedTo(name))
        .toList();
    }

    long getLowPulsesCount() {
      return lowPulsesCount;
    }

    long getHighPulsesCount() {
      return highPulsesCount;
    }

  }

  abstract static class Module {

    private final Modules modules;
    private final String name;
    private final List<String> destinations;

    Module(Modules modules, String name, String destinations) {
      this.modules = modules;
      this.name = name;
      this.destinations = Stream.of(destinations.split(", ")).toList();
    }

    String name() {
      return name;
    }

    boolean isConnectedTo(String destination) {
      return destinations.contains(destination);
    }

    abstract void receive(Module source, Pulse pulse);

    void send(Pulse pulse) {
      modules.sendPulse(this, pulse, destinations);
    }

    @Override
    public String toString() {
      return name + "(" + getClass().getSimpleName() + ")";
    }

  }

  private static class Broadcaster extends Module {

    Broadcaster(Modules modules, String destinations) {
      super(modules, "broadcaster", destinations);
    }

    @Override
    public void receive(Module source, Pulse pulse) {
      send(pulse);
    }

  }

  private static class FlipFlop extends Module {

    private State state = State.OFF;

    private FlipFlop(Modules modules, String name, String destinations) {
      super(modules, name, destinations);
    }

    @Override
    public void receive(Module source, Pulse pulse) {
      if (pulse == Pulse.HIGH) return;

      if (state == State.OFF) {
        state = State.ON;
        send(Pulse.HIGH);
      } else {
        state = State.OFF;
        send(Pulse.LOW);
      }
    }

    private enum State {
      ON, OFF
    }

  }

  private static class Conjunction extends Module {

    private final Supplier<List<Module>> supplier;

    private Map<Module, Pulse> pulseMemory;

    private Conjunction(Modules modules, String name, String destinations) {
      super(modules, name, destinations);

      supplier = () -> modules.getConnectedModules(name);
    }

    @Override
    public void receive(Module source, Pulse pulse) {
      if (pulseMemory == null) {
        pulseMemory = supplier.get().stream().collect(toMap(identity(), module -> Pulse.LOW));
      }

      pulseMemory.put(source, pulse);

      if (pulseMemory.values().stream().allMatch(value -> value == Pulse.HIGH)) {
        send(Pulse.LOW);
      } else {
        send(Pulse.HIGH);
      }
    }

  }

  private enum Pulse {
    HIGH, LOW
  }

}
