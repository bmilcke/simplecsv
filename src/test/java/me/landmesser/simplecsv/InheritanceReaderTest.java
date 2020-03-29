package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.Inherited;
import me.landmesser.simplecsv.types.InheritedBaseFirstSingle;
import me.landmesser.simplecsv.types.InheritedBaseLast;
import me.landmesser.simplecsv.types.InheritedFirstThenLast;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InheritanceReaderTest {

  @Test
  void noInheritance() throws IOException {
    String source = "foo,false";
    try (Reader stringReader = new StringReader(source);
         CSVReader<Inherited> reader = new CSVReader<>(
           stringReader, Inherited.class, CSVFormat.DEFAULT)) {
      List<Inherited> result = reader.read().collect(Collectors.toList());
      assertEquals(1, result.size());

      Inherited object = result.get(0);
      assertEquals("foo", object.getInheritedString());
      assertEquals(Boolean.FALSE, object.getInheritedBool());
      assertEquals(0, object.getBaseInt());
      assertNull(object.getBaseString());
    }
  }

  @Test
  void baseFirstSingle() throws IOException {
    String source = "inherited value,true,-12";
    try (Reader stringReader = new StringReader(source);
         CSVReader<InheritedBaseFirstSingle> reader = new CSVReader<>(
           stringReader, InheritedBaseFirstSingle.class, CSVFormat.DEFAULT)) {
      List<InheritedBaseFirstSingle> result = reader.read().collect(Collectors.toList());
      assertEquals(1, result.size());

      InheritedBaseFirstSingle object = result.get(0);
      assertEquals("inherited value", object.getInheritedString());
      assertEquals(Boolean.TRUE, object.getInheritedBool());
      assertEquals(-12, object.getOwnField());
      assertEquals(0, object.getBaseInt());
      assertNull(object.getBaseString());
    }
  }

  @Test
  void baseLast() throws IOException {
    String source = "str,inherit-str,,base-str,-42";
    try (Reader stringReader = new StringReader(source);
         CSVReader<InheritedBaseLast> reader = new CSVReader<>(
           stringReader, InheritedBaseLast.class, CSVFormat.DEFAULT)) {
      List<InheritedBaseLast> result = reader.read().collect(Collectors.toList());
      assertEquals(1, result.size());
      InheritedBaseLast object = result.get(0);

      assertEquals("str", object.getOwnField());
      assertEquals("inherit-str", object.getInheritedString());
      assertNull(object.getInheritedBool());
      assertEquals("base-str", object.getBaseString());
      assertEquals(-42, object.getBaseInt());
    }
  }

  @Test
  void firstThenLast() throws IOException {
    String source = "intermediate,Inherited,false,Base,19,16777216";
    try (Reader stringReader = new StringReader(source);
         CSVReader<InheritedFirstThenLast> reader = new CSVReader<>(
           stringReader, InheritedFirstThenLast.class, CSVFormat.DEFAULT)) {
      List<InheritedFirstThenLast> result = reader.read().collect(Collectors.toList());
      assertEquals(1, result.size());
      InheritedFirstThenLast object = result.get(0);

      assertEquals("intermediate", object.getOwnField() );
      assertEquals("Inherited", object.getInheritedString());
      assertEquals(Boolean.FALSE, object.getInheritedBool());
      assertEquals("Base", object.getBaseString());
      assertEquals(19, object.getBaseInt());
      assertEquals(16_777_216L, object.getMyOwnLong());
    }
  }
}
