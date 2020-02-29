package me.landmesser.csv;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class CSVEntry<T> {

  public CSVEntry(Class<T> type, Field field) {
    this.type = Objects.requireNonNull(type);
    this.fieldName = Objects.requireNonNull(field).getName();
    determineConverter(field);
    determineName(field);
  }

  private String name;
  private final Class<T> type;
  private String fieldName;
  private Function<T, String> converter;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Class<T> getType() {
    return type;
  }

  public String getFieldName() {
    return fieldName;
  }

  public Function<T, String> getConverter() {
    return converter;
  }

  private void determineName(Field field) {
    name = Arrays.stream(field.getAnnotationsByType(CSVColumnName.class))
      .findFirst()
      .map(CSVColumnName::value)
      .orElse(StringUtils.capitalize(field.getName()));
  }

  private void determineConverter(Field field) {
    converter = Arrays.stream(field.getAnnotationsByType(CSVDateFormat.class))
      .findFirst()
      .map(CSVDateFormat::value)
      .map(fmt -> {
        if (field.getType().isAssignableFrom(LocalDate.class)) {
          return (Function<T, String>)ld -> ((LocalDate)ld).format(DateTimeFormatter.ofPattern(fmt));
        }
        return null;
      }).orElse(null);
  }
}
