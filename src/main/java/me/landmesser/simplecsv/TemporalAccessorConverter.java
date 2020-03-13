package me.landmesser.simplecsv;

import me.landmesser.simplecsv.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemporalAccessorConverter {
  private static final Map<Class<? extends TemporalAccessor>, DateTimeFormatter> DATE_TIME_FORMATTERS =
    Stream.of(
      Pair.of(LocalDate.class, DateTimeFormatter.ISO_LOCAL_DATE),
      Pair.of(LocalTime.class, DateTimeFormatter.ISO_LOCAL_TIME),
      Pair.of(LocalDateTime.class, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
      Pair.of(ZonedDateTime.class, DateTimeFormatter.ISO_DATE_TIME),
      Pair.of(Instant.class, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    ).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

  public <T extends TemporalAccessor> String convert(T value, Class<T> type) {
    if (value == null) {
      return null;
    }
    if (DATE_TIME_FORMATTERS.containsKey(type)) {
      return DATE_TIME_FORMATTERS.get(type).format(value);
    } else {
      return value.toString();
    }
  }

  public <T extends TemporalAccessor> String convertWithFormat(T value, Class<T> type, String datePattern) {
    if (value == null) {
      return null;
    }
    return DateTimeFormatter.ofPattern(datePattern).format(value);
  }

  public <T> T parse(String value, Class<T> type) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    try {
      if (TemporalAccessor.class.isAssignableFrom(type) && DATE_TIME_FORMATTERS.containsKey(type)) {
        TemporalAccessor result = DATE_TIME_FORMATTERS.get(type).parse(value);
        Method fromMethod = type.getDeclaredMethod("from", TemporalAccessor.class);
        Object fromResult = fromMethod.invoke(null, result);
        if (type.isInstance(fromResult)) {
          return type.cast(fromResult);
        }
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new CSVConversionException("Could not parse instant", e);
    }
    return null;
  }

  public <T extends TemporalAccessor> T parseWithFormat(String value, Class<T> type, String datePattern) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    try {
      Method fromMehtod = type.getDeclaredMethod("from", TemporalAccessor.class);
      Object fromResult = fromMehtod.invoke(null, DateTimeFormatter.ofPattern(datePattern).parse(value));
      if (type.isInstance(fromResult)) {
        return type.cast(fromResult);
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new CSVConversionException("Could not parse instant with given format", e);
    }
    return null;
  }
}
