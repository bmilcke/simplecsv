package me.landmesser.csv;

import me.landmesser.csv.exception.CSVWriteException;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVWriterTest {

  @Test
  void unannotated() throws IOException, CSVWriteException {
    CSVWriter<Unannotated> writer = new CSVWriter<>(Unannotated.class).withFormat(
      CSVFormat.RFC4180.withDelimiter(';'));

    Unannotated object = new Unannotated();
    object.setAge(42);
    object.setSurname("Smith");
    object.setDateOfBirth(LocalDate.of(1980, 4, 2));

    String result = retrieveCSVString(writer, object);
    List<String> resultLines = Arrays.asList(result.split("\\r?\\n"));
    assertEquals(2, resultLines.size());
    assertEquals("Firstname;Surname;Age;DateOfBirth;Member;WantsEmail;MemberSinceYear", resultLines.get(0));
    assertEquals(";Smith;42;1980-04-02;false;;", resultLines.get(1));

    object.setWantsEmail(true);
    object.setMember(true);
    object.setMemberSinceYear(2010);

    result = retrieveCSVString(writer, object);
    resultLines = Arrays.asList(result.split("\\r?\\n"));
    assertEquals(2, resultLines.size());
    assertEquals(";Smith;42;1980-04-02;true;true;2010", resultLines.get(1));
  }

  @Test
  void annotated() throws IOException, CSVWriteException {
    CSVWriter<Annotated> writer = new CSVWriter<>(Annotated.class).withFormat(
      CSVFormat.EXCEL.withDelimiter(';'));

    Annotated object = new Annotated();
    object.setName("Test");
    object.setBegin(LocalDate.of(2019, 3, 25));
    object.setValue(42.1234d);

    String result = retrieveCSVString(writer, object);
    List<String> resultLines = Arrays.asList(result.split("\\r?\\n"));
    assertEquals(2, resultLines.size());
    assertEquals("Custom;Begin;Unannotated;Value", resultLines.get(0));
    assertEquals("Test;25.03.2019;;42,12", resultLines.get(1));
  }

  private <T> String retrieveCSVString(CSVWriter<T> writer, T object) throws IOException, CSVWriteException {
    try (StringWriter sw = new StringWriter()) {
      writer.write(sw, Stream.of(object));
      return sw.toString();
    }
  }
}
