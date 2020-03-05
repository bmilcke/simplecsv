package me.landmesser.simplecsv;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

class CSVEntry<T> {

  private final Class<T> type;
  private final String fieldName;
  private String name;
  private CSVConverter<T> converter;

  public CSVEntry(Class<T> type, Field field, ColumnNameStyle columnNameStyle) {
    this.type = Objects.requireNonNull(type);
    this.fieldName = Objects.requireNonNull(field).getName();
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
      .map(fmt -> new CSVDateConverter(field.getType(), fmt)).orElse(null);
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
