package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVConversionException;
import me.landmesser.simplecsv.CSVConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

public class MyLocalDateTimeConverter implements CSVConverter<LocalDateTime> {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
    .withLocale(Locale.GERMAN);

  @Override
  public String convert(LocalDateTime value) {
    if (value == null) {
      return null;
    }
    return value.format(formatter);
  }

  @Override
  public LocalDateTime parse(String value) throws CSVConversionException {
    try {
      if (value == null) {
        return null;
      }
      return LocalDateTime.parse(value, formatter);
    } catch (DateTimeParseException ex) {
      throw new CSVConversionException("Could not parse LocalDateTime", ex);
    }
  }
}
