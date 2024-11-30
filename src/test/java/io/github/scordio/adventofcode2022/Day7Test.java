package io.github.scordio.adventofcode2022;

import io.github.scordio.ScanInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.list;
import static java.nio.file.Files.size;
import static java.nio.file.Files.walk;
import static java.nio.file.Files.write;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 7: No Space Left On Device")
class Day7Test {

  @TempDir
  private Path root;

  @ParameterizedTest
  @CsvSource({
    "day7-example, 95437",
    "day7, 919137",
  })
  void part1(@ScanInput Scanner scanner, int expected) throws IOException {
    Path pwd = root;

    while (scanner.hasNextLine()) {
      String[] parts = scanner.nextLine().split(" ");

      if (parts[0].equals("$")) {
        if (parts[1].equals("cd") && !parts[2].equals("/")) {
          pwd = parts[2].equals("..") ? pwd.getParent() : pwd.resolve(parts[2]);
        }
      } else {
        try {
          int size = parseInt(parts[0]);
          Path file = createFile(pwd.resolve(parts[1]));
          write(file, new byte[size]);
        } catch (NumberFormatException e) {
          createDirectory(pwd.resolve(parts[1]));
        }
      }
    }

    try (var content = walk(root)) {
      long answer = content.filter(Files::isDirectory)
        .mapToLong(Day7Test::directorySize)
        .filter(size -> size <= 100000)
        .sum();

      assertEquals(expected, answer);
    }
  }

  @ParameterizedTest
  @CsvSource({
    "day7-example, 24933642",
    "day7, 2877389",
  })
  void part2(@ScanInput Scanner scanner, int expected) throws IOException {
    Path pwd = root;

    while (scanner.hasNextLine()) {
      String[] parts = scanner.nextLine().split(" ");

      if (parts[0].equals("$")) {
        if (parts[1].equals("cd") && !parts[2].equals("/")) {
          pwd = parts[2].equals("..") ? pwd.getParent() : pwd.resolve(parts[2]);
        }
      } else {
        try {
          int size = parseInt(parts[0]);
          Path file = createFile(pwd.resolve(parts[1]));
          write(file, new byte[size]);
        } catch (NumberFormatException e) {
          createDirectory(pwd.resolve(parts[1]));
        }
      }
    }

    long needed = 30000000 - (70000000 - directorySize(root));

    try (var content = walk(root)) {
      long answer = content.filter(Files::isDirectory)
        .mapToLong(Day7Test::directorySize)
        .filter(size -> size >= needed)
        .min()
        .orElseThrow();

      assertEquals(expected, answer);
    }
  }

  private static long directorySize(Path directory) {
    try (var content = list(directory)) {
      return content.mapToLong(path -> {
          try {
            return isDirectory(path) ? directorySize(path) : size(path);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .sum();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
