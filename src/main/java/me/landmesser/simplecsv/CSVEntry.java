package me.landmesser.simplecsv;

import me.landmesser.simplecsv.annotation.CSVColumnName;
import me.landmesser.simplecsv.annotation.CSVDateFormat;
import me.landmesser.simplecsv.annotation.CSVUseConverter;
import me.landmesser.simplecsv.converter.CSVConverter;
import me.landmesser.simplecsv.converter.CSVDateConverter;
import me.landmesser.simplecsv.converter.CSVUtilDateConverter;
import me.landmesser.simplecsv.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class CSVEntry<T> {

  private final Class<T> type;
  private final String fieldName;
  private String name;
  private CSVConverter<T> converter;
  private final Class<?> genericType;

  public CSVEntry(Class<T> type, Field field, ColumnNameStyle columnNameStyle) {
    this.type = Objects.requireNonNull(type);
    this.fieldName = Objects.requireNonNull(field).getName();
    genericType = Optional.ofNullable(field.getGenericType())
      .filter(ParameterizedType.class::isInstance)
      .map(ParameterizedType.class::cast)
      .map(ParameterizedType::getActualTypeArguments)
      .filter(arg -> arg.length > 0)
      .map(arg -> arg[0])
      .filter(Class.class::isInstance)
      .map(Class.class::cast).orElse(null);
    determineConverter(field);
    determineName(field, columnNameStyle);
    determineConverterclass(field);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Class<T> getType() {
    return type;
  }

  public Class<?> getGenericType() {
    return genericType;
  }

  public String getFieldName() {
    return fieldName;
  }

  public CSVConverter<T> getConverter() {
    return converter;
  }

  private void determineName(Field field, ColumnNameStyle columnNameStyle) {
    name = Arrays.stream(field.getAnnotationsByType(CSVColumnName.class))
      .findFirst()
      .map(CSVColumnName::value)
      .orElseGet(() -> getDefaultColumnName(field.getName(),
        columnNameStyle == null ? ColumnNameStyle.CAPITALIZED : columnNameStyle));
  }

  private String getDefaultColumnName(String name, ColumnNameStyle columnNameStyle) {
    switch (columnNameStyle) {
      case LIKE_FIELD:
        return name;
      case UPPERCASE:
        return name.toUpperCase();
      case LOWERCASE:
        return name.toLowerCase();
      case CAPITALIZED:
      default:
        return StringUtils.capitalize(name);
    }
  }

  @SuppressWarnings("unchecked, rawtypes")
  private void determineConverter(Field field) {
    converter = Arrays.stream(field.getAnnotationsByType(CSVDateFormat.class))
      .findFirst()
      .map(CSVDateFormat::value)
      .map(fmt ->
        (Date.class.isAssignableFrom(field.getType())) ? new CSVUtilDateConverter(fmt)
          : new CSVDateConverter(field.getType(), fmt)).orElse(null);
    if (converter == null) {
      if (List.class.isAssignableFrom(type) && genericType != null) {
        converter = new ListConverter(genericType);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void determineConverterclass(Field field) {
    Arrays.stream(field.getAnnotationsByType(CSVUseConverter.class))
      .findFirst()
      .map(CSVUseConverter::value)
      .map(aClass -> {
        try {
          return aClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
          return null;
        }
      }).ifPresent(inst -> converter = inst);
  }
}
