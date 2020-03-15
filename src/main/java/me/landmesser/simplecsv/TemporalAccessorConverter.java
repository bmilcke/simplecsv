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

public class TemporalAccessorConverter<T extends TemporalAccessor> implements CSVConverter<T> {

  private static final Map<Class<? extends TemporalAccessor>, DateTimeFormatter> DATE_TIME_FORMATTERS =
    Stream.of(
      Pair.of(LocalDate.class, DateTimeFormatter.ISO_LOCAL_DATE),
      Pair.of(LocalTime.class, DateTimeFormatter.ISO_LOCAL_TIME),
      Pair.of(LocalDateTime.class, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
      Pair.of(ZonedDateTime.class, DateTimeFormatter.ISO_DATE_TIME),
      Pair.of(Instant.class, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    ).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

  private final Class<T> type;
  private final DateTimeFormatter customFormatter;

  public TemporalAccessorConverter(Class<T> type) {
    this.type = type;
    customFormatter = null;
  }

  public TemporalAccessorConverter(Class<T> type, String dfPattern) {
    this.type = type;
    this.customFormatter = DateTimeFormatter.ofPattern(dfPattern);
  }

  @Override
  public String convert(T value) {
    if (value == null) {
      return null;
    }
    if (customFormatter != null) {
      return customFormatter.format(value);
    }
    if (DATE_TIME_FORMATTERS.containsKey(type)) {
      return DATE_TIME_FORMATTERS.get(type).format(value);
    } else {
      return value.toString();
    }
  }

  @Override
  public T parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    TemporalAccessor result;
    if (customFormatter != null) {
      result = customFormatter.parse(value);
    } else if (DATE_TIME_FORMATTERS.containsKey(type)) {
      result = DATE_TIME_FORMATTERS.get(type).parse(value);
    } else {
      throw new CSVConversionException("No DateFormatter found");
    }
    try {
      Method fromMethod = type.getDeclaredMethod("from", TemporalAccessor.class);
      Object fromResult = fromMethod.invoke(null, result);
      if (type.isInstance(fromResult)) {
        return type.cast(fromResult);
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new CSVConversionException("Could not parse instant", e);
    }
    throw new CSVConversionException("Result has incorrect type");
  }
}
