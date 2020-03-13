package me.landmesser.simplecsv;

import me.landmesser.simplecsv.UtilDateConverter;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilDateConverterTest {

  @Test
  void convert() {
    Date date = new Date(1591891473000L);
    UtilDateConverter converter = new UtilDateConverter();
    String result = converter.convert(date);
    assertEquals("2020-06-11T16:04:33Z[UTC]", result);
  }

  @Test
  void parse() {
    String dateStr = "2020-06-11T17:03:21+01:00";
    UtilDateConverter converter = new UtilDateConverter();
    Date result = converter.parse(dateStr);
    assertEquals(1591891401000L, result.getTime());
  }
}
