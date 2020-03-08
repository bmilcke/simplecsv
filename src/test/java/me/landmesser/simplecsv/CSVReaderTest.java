package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.TestEnum;
import me.landmesser.simplecsv.types.Types;
import me.landmesser.simplecsv.types.Unannotated;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CSVReaderTest {

  @Test
  void unannotated() throws IOException {
    try (InputStream inp = getClass().getResourceAsStream("/unannotated.csv");
         Reader stringReader = new InputStreamReader(inp);
         CSVReader<Unannotated> reader = new CSVReader<>(
           stringReader, Unannotated.class,
           CSVFormat.RFC4180.withDelimiter(',')
             .withFirstRecordAsHeader().withSkipHeaderRecord())) {
      List<Unannotated> result = reader.read().collect(Collectors.toList());
      assertEquals(1, result.size());

      Unannotated resultObject = result.get(0);
      assertNull(resultObject.getFirstname());
      assertEquals("Smith", resultObject.getSurname());
      assertEquals(42, resultObject.getAge());
      assertEquals(LocalDate.of(1980, 4, 2), resultObject.getDateOfBirth());
      assertTrue(resultObject.isMember());
      assertNotNull(resultObject.getWantsEmail());
      assertTrue(resultObject.getWantsEmail());
      assertEquals(2010, resultObject.getMemberSinceYear());
      assertEquals(TestEnum.SECOND, resultObject.getTestEnum());
    }
  }

  @Test
  void allTypes() throws IOException {
    try (InputStream inp = getClass().getResourceAsStream("/types.csv");
         Reader stringReader = new InputStreamReader(inp);
         CSVReader<Types> reader = new CSVReader<>(
           stringReader, Types.class,
           CSVFormat.RFC4180.withDelimiter(',')
             .withFirstRecordAsHeader().withSkipHeaderRecord())) {
      List<Types> result = reader.read().collect(Collectors.toList());
      assertEquals(2, result.size());

      Types t1 = result.get(0);
      assertEquals("One, Two, Three", t1.getString());
      assertEquals(42, t1.getPrimInt());
      assertEquals('x', t1.getPrimChar());
      assertFalse(t1.isPrimBoolean());
      assertEquals(256, t1.getInteger());
      assertEquals('*', t1.getCharacter());
      assertNull(t1.getBoleanVal());
      assertEquals(BigDecimal.valueOf(32545, 2), t1.getBigDecimal());
      assertEquals(LocalTime.of(3, 22, 3, 100_000_000), t1.getLocalTime());
      assertEquals(LocalDate.of(2020, 2, 21), t1.getLocalDate());
      assertEquals(LocalDateTime.of(2020, 1, 12, 22, 13, 2), t1.getLocalDateTime());
      assertEquals(TestEnum.THIRD, t1.getTestEnum());
    }
  }
}
