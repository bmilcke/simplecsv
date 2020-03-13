package me.landmesser.simplecsv.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

  @ParameterizedTest
  @CsvSource({
    ",",
    "'',''",
    "' ',' '",
    "\"###\",\"###\"",
    "\"\",\"\"",
    "a,A",
    "ab,Ab",
    "A,A",
    "AB,AB",
    "Ab,Ab",
    "öl,Öl",
    "littlelonger,Littlelonger",
    "camelCase,CamelCase"
  })
  void capitalize(final String in, final String expected) {
    assertEquals(expected, StringUtils.capitalize(in));
  }
}
