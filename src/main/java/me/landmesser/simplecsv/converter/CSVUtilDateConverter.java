package me.landmesser.simplecsv.converter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class CSVUtilDateConverter implements CSVConverter<Date> {

  private final String dfPattern;
  private final TemporalAccessorConverter taConverter = new TemporalAccessorConverter();

  public CSVUtilDateConverter(String dfPattern) {
    this.dfPattern = dfPattern;
  }

  @Override
  public String convert(Date value) {
    if (value == null) {
      return null;
    }
    ZonedDateTime zdt = ZonedDateTime.ofInstant(value.toInstant(), ZoneId.of("UTC"));
    return taConverter.convertWithFormat(zdt, ZonedDateTime.class, dfPattern);
  }

  @Override
  public Date parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    return Date.from(taConverter.parseWithFormat(value, Instant.class, dfPattern));
  }
}
