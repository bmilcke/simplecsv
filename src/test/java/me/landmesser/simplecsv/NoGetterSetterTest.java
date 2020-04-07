package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.NoGetterSetter;
import me.landmesser.simplecsv.types.RequireGetter;
import me.landmesser.simplecsv.types.RequireGetterAndSetter;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NoGetterSetterTest {

  @Test
  void testStrategyAll() throws IOException {
    NoGetterSetter object = new NoGetterSetter();
    object.setNoGetter(42d);
    object.setFieldToExport(LocalDate.of(2019, 3, 25));

    List<String> writerResult = CSVWriterTest.retrieveCSVString(object, NoGetterSetter.class, null, true);

    assertEquals(2, writerResult.size());
    assertEquals("NoSetterGetter,NoSetter,NoBoolSetter,NoGetter,FieldToExport", writerResult.get(0));
    assertEquals(",,false,,2019-03-25", writerResult.get(1));

    String source = "1,foo,true,42.0,1972-06-11";
    try (Reader stringReader = new StringReader(source);
         CSVReader<NoGetterSetter> reader = new CSVReader<>(
           stringReader, NoGetterSetter.class, CSVFormat.DEFAULT)) {
      List<NoGetterSetter> readResult = reader.read().collect(Collectors.toList());
      assertEquals(1, readResult.size());
      NoGetterSetter resultObject = readResult.get(0);
      assertEquals(LocalDate.of(1972, 6, 11), resultObject.getFieldToExport());
      assertNull(resultObject.getNoSetter());
      assertFalse(resultObject.isNoBoolSetter());
    }
  }

  @Test
  void testStrategyGetter() throws IOException {
    RequireGetter object = new RequireGetter();
    object.setNoGetter(42d);
    object.setFieldToExport(LocalDate.of(2019, 3, 25));

    List<String> writerResult = CSVWriterTest.retrieveCSVString(object, RequireGetter.class, null, true);

    assertEquals(2, writerResult.size());
    assertEquals("NoSetter,NoBoolSetter,FieldToExport", writerResult.get(0));
    assertEquals(",false,2019-03-25", writerResult.get(1));

    String source = "foo,true,1972-06-11";
    try (Reader stringReader = new StringReader(source);
         CSVReader<RequireGetter> reader = new CSVReader<>(
           stringReader, RequireGetter.class, CSVFormat.DEFAULT)) {
      List<NoGetterSetter> readResult = reader.read().collect(Collectors.toList());
      assertEquals(1, readResult.size());
      NoGetterSetter resultObject = readResult.get(0);
      assertEquals(LocalDate.of(1972, 6, 11), resultObject.getFieldToExport());
      assertNull(resultObject.getNoSetter());
      assertFalse(resultObject.isNoBoolSetter());
    }
  }

  @Test
  void testStrategyGetterAndSetter() throws IOException {
    RequireGetterAndSetter object = new RequireGetterAndSetter();
    object.setNoGetter(42d);
    object.setFieldToExport(LocalDate.of(2019, 3, 25));

    List<String> writerResult = CSVWriterTest.retrieveCSVString(object, RequireGetterAndSetter.class, null, true);

    assertEquals(2, writerResult.size());
    assertEquals("FieldToExport", writerResult.get(0));
    assertEquals("2019-03-25", writerResult.get(1));

    String source = "1972-06-11";
    try (Reader stringReader = new StringReader(source);
         CSVReader<RequireGetterAndSetter> reader = new CSVReader<>(
           stringReader, RequireGetterAndSetter.class, CSVFormat.DEFAULT)) {
      List<NoGetterSetter> readResult = reader.read().collect(Collectors.toList());
      assertEquals(1, readResult.size());
      NoGetterSetter resultObject = readResult.get(0);
      assertEquals(LocalDate.of(1972, 6, 11), resultObject.getFieldToExport());
      assertNull(resultObject.getNoSetter());
      assertFalse(resultObject.isNoBoolSetter());
    }
  }
}
