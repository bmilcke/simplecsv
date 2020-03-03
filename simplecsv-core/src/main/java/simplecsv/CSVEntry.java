package simplecsv;

import me.landmesser.simplecsv.CSVColumnName;
import me.landmesser.simplecsv.CSVConverter;
import me.landmesser.simplecsv.CSVDateFormat;
import me.landmesser.simplecsv.CSVUseConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

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
  private final String fieldName;
  private CSVConverter<T> converter;

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

  private void determineName(Field field) {
    name = Arrays.stream(field.getAnnotationsByType(CSVColumnName.class))
      .findFirst()
      .map(CSVColumnName::value)
      .orElse(StringUtils.capitalize(field.getName()));
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
