package me.landmesser.csv;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CSVWriterTest {

  @Test
  void unannotated() {
    CSVWriter<Unannotated> writer = new CSVWriter<>(Unannotated.class);
    assertEquals("Firstname;Surname;Age;DateOfBirth;Member;WantsEmail;MemberSinceYear",
      writer.retrieveHeaders());

    Unannotated object = new Unannotated();
    object.setAge(42);
    object.setSurname("Smith");
    object.setDateOfBirth(LocalDate.of(1980,4,2));

    assertEquals(";Smith;42;1980-04-02;false;;",
      writer.retrieveLine(object));

    object.setWantsEmail(true);
    object.setMember(true);
    object.setMemberSinceYear(2010);

    assertEquals(";Smith;42;1980-04-02;true;true;2010", writer.retrieveLine(object));
  }

  @Test
  void annotated() {
    CSVWriter<Annotated> writer = new CSVWriter<>(Annotated.class);
    assertEquals("Custom;Begin", writer.retrieveHeaders());

    Annotated object = new Annotated();
    object.setName("Test");
    object.setBegin(LocalDate.of(2019, 3, 25));

    assertEquals("Test;25.03.2019", writer.retrieveLine(object));
  }
}
