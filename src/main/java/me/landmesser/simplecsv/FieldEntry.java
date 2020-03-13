package me.landmesser.simplecsv;

import me.landmesser.simplecsv.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class FieldEntry<T> {

  private final Class<T> type;
  private final String fieldName;
  @SuppressWarnings("rawtypes")
  private final List<Class> genericTypes;
  private String name;
  private CSVConverter<T> converter;

  public FieldEntry(Class<T> type, Field field, ColumnNameStyle columnNameStyle) {
    this.type = Objects.requireNonNull(type);
    this.fieldName = Objects.requireNonNull(field).getName();
    genericTypes = Optional.ofNullable(field.getGenericType())
      .filter(ParameterizedType.class::isInstance)
      .map(ParameterizedType.class::cast)
      .map(ParameterizedType::getActualTypeArguments)
      .map(Arrays::stream)
      .map(str -> str
        .filter(Class.class::isInstance)
        .map(Class.class::cast)
        .collect(Collectors.toList()))
      .orElse(Collections.emptyList());
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

  @SuppressWarnings("rawtypes")
  public List<Class> getGenericTypes() {
    return genericTypes;
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
          : new FormattedDateConverter(field.getType(), fmt)).orElse(null);
    if (converter == null) {
      if (List.class.isAssignableFrom(type) && !genericTypes.isEmpty()) {
        converter = new ListConverter(genericTypes.get(0));
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
