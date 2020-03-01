package me.landmesser.csv;

import me.landmesser.csv.exception.CSVException;
import me.landmesser.csv.types.Annotated;
import me.landmesser.csv.types.AnnotatedWithError;
import me.landmesser.csv.types.TestEnum;
import me.landmesser.csv.types.Types;
import me.landmesser.csv.types.Unannotated;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CSVWriterTest {

  @Test
  void unannotated() throws CSVException {
    CSVWriter<Unannotated> writer = new CSVWriter<>(Unannotated.class).withFormat(
      CSVFormat.RFC4180.withDelimiter(';'));

    Unannotated object = new Unannotated();
    object.setAge(42);
    object.setSurname("Smith");
    object.setDateOfBirth(LocalDate.of(1980, 4, 2));

    List<String> result = retrieveCSVString(writer, object);
    assertEquals(2, result.size());
    assertEquals("Firstname;Surname;Age;DateOfBirth;Member;WantsEmail;MemberSinceYear;TestEnum", result.get(0));
    assertEquals(";Smith;42;1980-04-02;false;;;", result.get(1));

    object.setWantsEmail(true);
    object.setMember(true);
    object.setMemberSinceYear(2010);
    object.setTestEnum(TestEnum.SECOND);

    result = retrieveCSVString(writer, object);
    assertEquals(2, result.size());
    assertEquals(";Smith;42;1980-04-02;true;true;2010;SECOND", result.get(1));
  }

  @Test
  void annotated() throws CSVException {
    CSVWriter<Annotated> writer = new CSVWriter<>(Annotated.class).withFormat(
      CSVFormat.EXCEL.withDelimiter(';'));

    Annotated object = new Annotated();
    object.setName("Test");
    object.setBegin(LocalDate.of(2019, 3, 25));
    object.setValue(42.1234d);
    object.setEnd(LocalDateTime.of(
      LocalDate.of(2020, 3, 25),
      LocalTime.of(17, 23, 3, 15)));

    List<String> result = retrieveCSVString(writer, object);
    assertEquals(2, result.size());
    assertEquals("Custom;Begin;End;Unannotated;Value;Checked", result.get(0));
    assertEquals("Test;25.03.2019;Mittwoch, 25. März 2020;;42,12;falsch", result.get(1));
  }

  @Test
  void testNullValues() throws CSVException {
    CSVWriter<Types> writer = new CSVWriter<>(Types.class);

    Types object = new Types();

    List<String> result = retrieveCSVString(writer, object);
    assertEquals(2, result.size());
    assertEquals("String,PrimInt,PrimChar,PrimBoolean,Integer,Character,BoleanVal", result.get(0));
    assertEquals(String.format(",0,\"%c\",false,,,", 0), result.get(1));

    object.setString("");
    object.setPrimInt(2);
    object.setPrimChar('ö');
    object.setPrimBoolean(true);
    object.setInteger(5);
    object.setCharacter('#');
    object.setBooleanVal(false);

    result = retrieveCSVString(writer, object);
    assertEquals(2, result.size());
    assertEquals("String,PrimInt,PrimChar,PrimBoolean,Integer,Character,BoleanVal", result.get(0));
    assertEquals("\"\",2,ö,true,5,\"#\",false", result.get(1));
  }

  @Test
  void testQuoting() throws CSVException {
    CSVWriter<Types> writer = new CSVWriter<>(Types.class);

    Types object = new Types();
    object.setPrimChar('x');
    object.setString("A string with spaces");

    List<String> result = retrieveCSVString(writer, object);
    assertEquals(2, result.size());
    assertEquals("A string with spaces,0,x,false,,,", result.get(1));

    object.setString("A string with spaces, and also commas");

    result = retrieveCSVString(writer, object);
    assertEquals(2, result.size());
    assertEquals("\"A string with spaces, and also commas\",0,x,false,,,", result.get(1));

    object.setString("This one with \"");

    result = retrieveCSVString(writer, object);
    assertEquals(2, result.size());
    assertEquals("\"This one with \"\"\",0,x,false,,,", result.get(1));
  }

  @Test
  void classAnnotationError() {
    assertThrows(CSVException.class, () -> new CSVWriter<>(AnnotatedWithError.class));
  }

  private <T> List<String> retrieveCSVString(CSVWriter<T> writer, T object) throws CSVException {
    try (StringWriter sw = new StringWriter()) {
      writer.write(sw, Stream.of(object));
      return Arrays.asList(sw.toString().split("\\r?\\n"));
    } catch (IOException ex) {
      throw new CSVException("Wrapped IOException", ex);
    }
  }
}
