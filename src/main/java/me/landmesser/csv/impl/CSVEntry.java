package me.landmesser.csv.impl;

import me.landmesser.csv.CSVConverter;
import me.landmesser.csv.ConverterParseFunction;
import me.landmesser.csv.annotation.CSVColumnName;
import me.landmesser.csv.annotation.CSVConvert;
import me.landmesser.csv.annotation.CSVDateFormat;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    determineConverterclass(field);
  }

  private String name;
  private final Class<T> type;
  private String fieldName;
  private Function<T, String> converter;
  private ConverterParseFunction<T> parser;

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

  public ConverterParseFunction<T> getParser() {
    return parser;
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

  @SuppressWarnings("unchecked")
  private void determineConverterclass(Field field) {
    CSVConverter<T> converterInstance = Arrays.stream(field.getAnnotationsByType(CSVConvert.class))
      .findFirst()
      .map(CSVConvert::value)
      .map(aClass -> {
        try {
          return aClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
          return null;
        }
      })
      .orElse(null);
    if (converterInstance != null) {
      converter = converterInstance::convert;
      parser = converterInstance::parse;
    }
  }
}
