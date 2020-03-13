package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.EnumConverterContainer;
import me.landmesser.simplecsv.types.TestEnum;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EnumConverterAnnotationTest {

  @ParameterizedTest
  @CsvSource({
    "FIRST,One",
    "SECOND,Two",
    "THIRD,Three",
    "FOURTH,Four"
  })
  void write(TestEnum enumVal, String expected) {
    EnumConverterContainer object = new EnumConverterContainer();
    object.setMyEnum(enumVal);
    List<String> result = CSVWriterTest.retrieveCSVString(object, EnumConverterContainer.class,
      CSVFormat.EXCEL, false);
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(expected, result.get(0));
  }

  @ParameterizedTest
  @CsvSource({
    "FIRST,One",
    "SECOND,Two",
    "THIRD,Three",
    "FOURTH,Four"
  })
  void read(TestEnum expected, String input) throws IOException {
    try (Reader stringReader = new StringReader(input);
         CSVReader<EnumConverterContainer> reader = new CSVReader<>(
           stringReader, EnumConverterContainer.class,
           CSVFormat.RFC4180)) {
      List<EnumConverterContainer> result = reader.read().collect(Collectors.toList());
      assertNotNull(result);
      assertEquals(1, result.size());
      assertEquals(expected, result.get(0).getMyEnum());
    }
  }
}
