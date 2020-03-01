package me.landmesser.csv;

import me.landmesser.csv.types.Unannotated;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CSVReaderTest {

  @Test
  void unannotated() throws IOException {
    CSVReader<Unannotated> reader = new CSVReader<>(Unannotated.class)
      .withFormat(CSVFormat.RFC4180.withDelimiter(';').withFirstRecordAsHeader());

    try (InputStream inp = getClass().getResourceAsStream("/unannotated.csv");
         Reader stringReader = new InputStreamReader(inp)) {
      List<Unannotated> result = reader.read(stringReader).collect(Collectors.toList());
      assertEquals(1, result.size());

      Unannotated resultObject = result.get(0);
      assertNull(resultObject.getFirstname());
      assertEquals("Smith", resultObject.getSurname());
    }
  }
}
