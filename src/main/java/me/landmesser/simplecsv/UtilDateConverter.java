package me.landmesser.simplecsv;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@SuppressWarnings("rawtypes, unchecked")
public class UtilDateConverter implements CSVConverter<Date> {

  private final TemporalAccessorConverter<ZonedDateTime> taConverter;

  public UtilDateConverter() {
    this.taConverter = new TemporalAccessorConverter(ZonedDateTime.class);
  }

  public UtilDateConverter(String dfPattern) {
    this.taConverter = new TemporalAccessorConverter(ZonedDateTime.class, dfPattern);
  }

  @Override
  public String convert(Date value) {
    if (value == null) {
      return null;
    }
    ZonedDateTime zdt = ZonedDateTime.ofInstant(value.toInstant(), ZoneId.of("UTC"));
    return taConverter.convert(zdt);
  }

  @Override
  public Date parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    return Date.from(Instant.from(taConverter.parse(value)));
  }
}
