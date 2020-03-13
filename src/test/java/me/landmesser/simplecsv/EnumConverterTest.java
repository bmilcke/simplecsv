package me.landmesser.simplecsv;

import me.landmesser.simplecsv.CSVConversionException;
import me.landmesser.simplecsv.EnumConverter;
import me.landmesser.simplecsv.types.TestEnum;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumConverterTest {

  @Test
  void convert() {
    EnumConverter<TestEnum> conv = new EnumConverter<>(TestEnum.class);
    assertNull(conv.convert(null));
    assertEquals("FIRST", conv.convert(TestEnum.FIRST));
    assertEquals("SECOND", conv.convert(TestEnum.SECOND));
    assertEquals("THIRD", conv.convert(TestEnum.THIRD));
    assertEquals("FOURTH", conv.convert(TestEnum.FOURTH));
  }

  @Test
  void parse() {
    EnumConverter<TestEnum> conv = new EnumConverter<>(TestEnum.class);
    assertNull(conv.parse(null));
    assertEquals(TestEnum.FIRST, conv.parse("FIRST"));
    assertEquals(TestEnum.SECOND, conv.parse("SECOND"));
    assertEquals(TestEnum.THIRD, conv.parse("THIRD"));
    assertEquals(TestEnum.FOURTH, conv.parse("FOURTH"));
  }

  @Test
  void convertWithMapping() {
    EnumConverter<TestEnum> conv = new EnumConverter<>(TestEnum.class, getMapping());
    assertNull(conv.convert(null));
    assertEquals("Erstens", conv.convert(TestEnum.FIRST));
    assertEquals("SECOND", conv.convert(TestEnum.SECOND));
    assertEquals("Drittens", conv.convert(TestEnum.THIRD));
    assertEquals("FOURTH", conv.convert(TestEnum.FOURTH));
  }

  @Test
  void parseWithMapping() {
    EnumConverter<TestEnum> conv = new EnumConverter<>(TestEnum.class, getMapping());
    assertNull(conv.parse(null));
    assertEquals(TestEnum.FIRST, conv.parse("Erstens"));
    assertThrows(CSVConversionException.class, () -> conv.parse("FIRST"));
    assertEquals(TestEnum.SECOND, conv.parse("SECOND"));
    assertEquals(TestEnum.THIRD, conv.parse("Drittens"));
    assertEquals(TestEnum.FOURTH, conv.parse("FOURTH"));
  }

  private Map<TestEnum, String> getMapping() {
    Map<TestEnum, String> mapping = new HashMap<>();
    mapping.put(TestEnum.FIRST, "Erstens");
    mapping.put(TestEnum.THIRD, "Drittens");
    return mapping;
  }
}
