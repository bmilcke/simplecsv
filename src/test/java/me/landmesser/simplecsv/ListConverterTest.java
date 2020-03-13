package me.landmesser.simplecsv;

import me.landmesser.simplecsv.ListConverter;
import me.landmesser.simplecsv.types.TestEnum;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListConverterTest {

  @Test
  void convert() {
    ListConverter<String> converter = new ListConverter<>(String.class);
    String result = converter.convert(Stream.of(
      "First", "Second, with comma", "Third; with semicolon", "Last"
    ).collect(Collectors.toList()));
    assertEquals("[First,\"Second, with comma\",Third; with semicolon,Last]", result);

    ListConverter<Integer> intConverter = new ListConverter<>(Integer.class);
    String result2 = intConverter.convert(Stream.of(
      12, 0, 42, 17, 9, -5
    ).collect(Collectors.toList()));
    assertEquals("[12,0,42,17,9,-5]", result2);
  }

  @Test
  void parse() {
    ListConverter<TestEnum> converter = new ListConverter<>(TestEnum.class);
    List<TestEnum> result = converter.parse("[FIRST,THIRD,SECOND,FIRST,FOURTH]");
    assertEquals(Stream.of(
      TestEnum.FIRST, TestEnum.THIRD, TestEnum.SECOND, TestEnum.FIRST, TestEnum.FOURTH
    ).collect(Collectors.toList()), result);
  }

  @Test
  void delimiter() {
    ListConverter<String> converter = new ListConverter<>(String.class, '(', ')');
    String result = converter.convert(Stream.of(
      "A","B","C"
    ).collect(Collectors.toList()));
    assertEquals("(A,B,C)", result);
  }
}
