package me.landmesser.simplecsv.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.logging.Logger;

public class CSVDateConverter<T extends TemporalAccessor> implements CSVConverter<TemporalAccessor> {

  private final Class<T> type;
  private final DateTimeFormatter formatter;

  public CSVDateConverter(Class<T> type, String format) {
    this.type = type;
    formatter = DateTimeFormatter.ofPattern(format);
  }

  @Override
  public String convert(TemporalAccessor value) {
    if (value == null) {
      return null;
    }
    try {
      Method format = type.getDeclaredMethod("format", DateTimeFormatter.class);
      return (String)format.invoke(value, formatter);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      Logger.getLogger(getClass().getSimpleName()).warning("Could not format temporal accessor");
    }
    return value.toString();
  }

  @Override
  public TemporalAccessor parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    try {
      Method format = type.getDeclaredMethod("parse", String.class, DateTimeFormatter.class);
      return type.cast(format.invoke(null, value, formatter));
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | DateTimeParseException e) {
      throw new CSVConversionException("Could not parse temporal accessor class of type " + type.getSimpleName(), e);
    }
  }
}
