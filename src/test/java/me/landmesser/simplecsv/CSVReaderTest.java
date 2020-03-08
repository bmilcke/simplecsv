package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.TestEnum;
import me.landmesser.simplecsv.types.Unannotated;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
