package io.github.scordio.adventofcode2015;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.stream.IntStream.iterate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Day 4: The Ideal Stocking Stuffer")
class Day4Test {

  @ParameterizedTest
  @CsvSource({
    "609043,  abcdef",
    "1048970, pqrstuv",
    "282749,  yzbqklnj",
  })
  void part1(int expected, String secretKey) {
    int answer = iterate(0, i -> !md5(secretKey + i).startsWith("00000"), i -> i + 1)
      .max()
      .orElseThrow() + 1;

    assertEquals(expected, answer);
  }

  @ParameterizedTest
  @CsvSource({
    "9962624,  yzbqklnj",
  })
  void part2(int expected, String secretKey) {
    int answer = iterate(0, i -> !md5(secretKey + i).startsWith("000000"), i -> i + 1)
      .max()
      .orElseThrow() + 1;

    assertEquals(expected, answer);
  }

  private static String md5(String input) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      return HexFormat.of().formatHex(md5.digest(input.getBytes(US_ASCII)));
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
  }

}
