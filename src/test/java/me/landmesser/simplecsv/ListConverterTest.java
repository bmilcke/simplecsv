package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.GermanBooleanConverter;
import me.landmesser.simplecsv.types.TestEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListConverterTest {

  @Test
  void convert() {
    ListConverter<String> converter = new ListConverter<>(String.class, new StringConverter());
    String result = converter.convert(Stream.of(
      "First", "Second, with comma", "Third; with semicolon", "Last"
    ).collect(Collectors.toList()));
    assertEquals("[First,\"Second, with comma\",Third; with semicolon,Last]", result);

    ListConverter<Integer> intConverter = new ListConverter<>(Integer.class, new IntegerConverter());
    String result2 = intConverter.convert(Stream.of(
      12, 0, 42, 17, 9, -5
    ).collect(Collectors.toList()));
    assertEquals("[12,0,42,17,9,-5]", result2);
  }

  @Test
  void parse() {
    ListConverter<TestEnum> converter = new ListConverter<>(TestEnum.class, new EnumConverter<>(TestEnum.class));
    List<TestEnum> result = converter.parse("[FIRST,THIRD,SECOND,FIRST,FOURTH]");
    assertEquals(Stream.of(
      TestEnum.FIRST, TestEnum.THIRD, TestEnum.SECOND, TestEnum.FIRST, TestEnum.FOURTH
    ).collect(Collectors.toList()), result);
  }

  @Test
  void delimiterWrite() {
    ListConverter<String> converter = new ListConverter<>(String.class, new StringConverter(), '(', ')', ';');
    String result = converter.convert(Stream.of(
      "A", "B", "C"
    ).collect(Collectors.toList()));
    assertEquals("(A;B;C)", result);
  }

  @Test
  void delimiterParse() {
    ListConverter<Boolean> converter = new ListConverter<>(Boolean.class, new GermanBooleanConverter(), '{', '}', '\t');
    String csvText = "{wahr\twahr\tfalsch\twahr\tfalsch\tfalsch}";
    List<Boolean> result = converter.parse(csvText);
    assertEquals(6, result.size());
    assertTrue(result.get(0));
    assertTrue(result.get(1));
    assertFalse(result.get(2));
    assertTrue(result.get(3));
    assertFalse(result.get(4));
    assertFalse(result.get(5));
  }

  @Test
  void nonSimpleTypeRoundtrip() {
    ListConverter<LocalDate> localDateListConverter = new ListConverter<>(LocalDate.class, new TemporalAccessorConverter<>(LocalDate.class));
    List<LocalDate> valueList = Stream.of(
      LocalDate.of(1985, 2, 3),
      LocalDate.of(1975, 12, 6),
      LocalDate.of(2020, 3, 13))
      .collect(Collectors.toList());

    String result = localDateListConverter.convert(
      valueList);
    assertEquals("[1985-02-03,1975-12-06,2020-03-13]", result);

    List<LocalDate> roundtripResult = localDateListConverter.parse(result);
    assertEquals(valueList, roundtripResult);

  }
}
