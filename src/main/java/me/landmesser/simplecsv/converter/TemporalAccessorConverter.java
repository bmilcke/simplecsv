package me.landmesser.simplecsv.converter;

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

  public <T> T parse(String value, Class<T> type) throws CSVConversionException {
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
      throw new CSVConversionException();
    }
    return null;
  }
}
