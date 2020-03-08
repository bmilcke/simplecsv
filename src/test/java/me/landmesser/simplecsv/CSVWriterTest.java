package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.Annotated;
import me.landmesser.simplecsv.types.AnnotatedWithError;
import me.landmesser.simplecsv.types.ColumnStyleUppercase;
import me.landmesser.simplecsv.types.TestEnum;
import me.landmesser.simplecsv.types.Types;
import me.landmesser.simplecsv.types.Unannotated;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CSVWriterTest {

  @Test
  void unannotated() throws CSVException {
    Unannotated object = new Unannotated();
    object.setAge(42);
    object.setSurname("Smith");
    object.setDateOfBirth(LocalDate.of(1980, 4, 2));

    List<String> result = retrieveCSVString(object, Unannotated.class,
      CSVFormat.RFC4180.withDelimiter(';'), true);
    assertEquals(2, result.size());
    assertEquals("Firstname;Surname;Age;DateOfBirth;Member;WantsEmail;MemberSinceYear;TestEnum", result.get(0));
    assertEquals(";Smith;42;1980-04-02;false;;;", result.get(1));

    object.setWantsEmail(true);
    object.setMember(true);
    object.setMemberSinceYear(2010);
    object.setTestEnum(TestEnum.SECOND);

    result = retrieveCSVString(object, Unannotated.class,
      CSVFormat.RFC4180.withDelimiter(';'), false);
    assertEquals(1, result.size());
    assertEquals(";Smith;42;1980-04-02;true;true;2010;SECOND", result.get(0));
  }

  @Test
  void withoutHeaders() throws CSVException {
    Unannotated object = new Unannotated();
    List<String> result = retrieveCSVString(object, Unannotated.class, null, false);
    assertEquals(1, result.size());
    assertEquals(",,0,,false,,,", result.get(0));
  }

  @Test
  void annotated() throws CSVException {
    Annotated object = new Annotated();
    object.setName("Test");
    object.setBegin(LocalDate.of(2019, 3, 25));
    object.setValue(42.1234d);
    object.setEnd(LocalDateTime.of(
      LocalDate.of(2020, 3, 25),
      LocalTime.of(17, 23, 3, 15)));

    List<String> result = retrieveCSVString(object, Annotated.class,
      CSVFormat.EXCEL.withDelimiter(','), true);
    assertEquals(2, result.size());
    assertEquals("Custom,Begin,End,Unannotated,Value,Checked", result.get(0));
    assertEquals("Test,25.03.2019,\"Mittwoch, 25. März 2020\",,\"42,12\",falsch", result.get(1));
  }

  @Test
  void testAllTypes() throws CSVException {
    Types object = new Types();

    List<String> result = retrieveCSVString(object, Types.class, null, true);
    assertEquals(2, result.size());
    assertEquals("String,PrimInt,PrimChar,PrimBoolean,Integer,Character,BoleanVal,BigDecimal,LocalTime,LocalDate,LocalDateTime,TestEnum",
      result.get(0));
    assertEquals(String.format(",0,\"%c\",false,,,,,,,,", 0), result.get(1));

    object.setString("");
    object.setPrimInt(2);
    object.setPrimChar('ö');
    object.setPrimBoolean(true);
    object.setInteger(5);
    object.setCharacter('#');
    object.setBooleanVal(false);
    object.setBigDecimal(BigDecimal.TEN);
    object.setLocalTime(LocalTime.of(4, 31, 7, 213_243_654));
    object.setLocalDate(LocalDate.of(2020, 3, 25));
    object.setLocalDateTime(LocalDateTime.of(2020, 2, 29, 17, 21, 44));
    object.setTestEnum(TestEnum.FOURTH);

    result = retrieveCSVString(object, Types.class, null, true);
    assertEquals(2, result.size());
    assertEquals("String,PrimInt,PrimChar,PrimBoolean,Integer,Character,BoleanVal,BigDecimal,LocalTime,LocalDate,LocalDateTime,TestEnum", result.get(0));
    assertEquals("\"\",2,ö,true,5,\"#\",false,10,04:31:07.213243654,2020-03-25,2020-02-29T17:21:44,FOURTH", result.get(1));
  }

  @Test
  void testQuoting() throws CSVException {
    Types object = new Types();
    object.setPrimChar('x');
    object.setString("A string with spaces");

    List<String> result = retrieveCSVString(object, Types.class, null, true);
    assertEquals(2, result.size());
    assertEquals("A string with spaces,0,x,false,,,,,,,,", result.get(1));

    object.setString("A string with spaces, and also commas");

    result = retrieveCSVString(object, Types.class, null, true);
    assertEquals(2, result.size());
    assertEquals("\"A string with spaces, and also commas\",0,x,false,,,,,,,,", result.get(1));

    object.setString("This one with \"");

    result = retrieveCSVString(object, Types.class, null, true);
    assertEquals(2, result.size());
    assertEquals("\"This one with \"\"\",0,x,false,,,,,,,,", result.get(1));
  }

  @Test
  void classAnnotationError() {
    assertThrows(CSVException.class, () -> new CSVWriter<>(new StringWriter(),
      AnnotatedWithError.class, null, false));
  }

  @Test
  void testCSVDefaultColumnName() {
    ColumnStyleUppercase obj = new ColumnStyleUppercase();

    List<String> result = retrieveCSVString(obj, ColumnStyleUppercase.class,
      CSVFormat.DEFAULT, true);
    assertFalse(result.isEmpty());
    assertEquals("FIRST,SECOND,ANOTHERONE", result.get(0));
  }

  private <T> List<String> retrieveCSVString(T object, Class<T> type, CSVFormat format, boolean withHeaders) throws CSVException {
    try (StringWriter sw = new StringWriter();
         CSVWriter<T> writer = new CSVWriter<>(sw, type, format, withHeaders)) {
      writer.write(Stream.of(object));
      return Arrays.asList(sw.toString().split("\\r?\\n"));
    } catch (IOException ex) {
      throw new CSVException("Wrapped IOException", ex);
    }
  }
}
