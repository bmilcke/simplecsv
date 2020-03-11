package me.landmesser.simplecsv.converter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class UtilDateConverter implements CSVConverter<Date> {

  private TemporalAccessorConverter taConverter = new TemporalAccessorConverter();

  @Override
  public String convert(Date value) {
    if (value == null) {
      return null;
    }
    ZonedDateTime zdt = ZonedDateTime.ofInstant(value.toInstant(), ZoneId.of("UTC"));
    return taConverter.convert(zdt, ZonedDateTime.class);
  }

  @Override
  public Date parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    return Date.from(taConverter.parse(value, Instant.class));
  }
}
