package me.landmesser.csv;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CSVWriter<T> {

  private final Class<T> type;
  private final List<CSVEntry> entries;
  private Converters converters = new Converters();

  public CSVWriter(Class<T> type) {
    this.type = type;
    entries = Arrays.stream(type.getDeclaredFields())
      .filter(this::isNotIgnored)
      .map(f -> new CSVEntry(f.getType(), f))
      .collect(Collectors.toList());
  }

  private boolean isNotIgnored(Field field) {
    return !Arrays.stream(field.getAnnotationsByType(CSVIgnore.class))
      .findAny().isPresent();
  }

  public String retrieveHeaders() {
    return entries.stream()
      .map(CSVEntry::getName)
      .collect(Collectors.joining(";"));
  }

  public String retrieveLine(T object) {
    return entries.stream()
      .map(entry -> {
        try {
          return evaluate(object, entry);
        } catch (CSVParseException e) {
          return "";
        }
      })
      .collect(Collectors.joining(";"));
  }

  public <R> String evaluate(T object, CSVEntry<R> entry) throws CSVParseException {
    if (object != null) {
      try {
        Method method = type.getDeclaredMethod(determineGetter(entry));
        Object result = method.invoke(object);
        Class<R> entryType = entry.getType();
        if (entry.getConverter() != null) {
          return entry.getConverter().apply(entryType.cast(result));
        }
        if (entryType.isInstance(result)) {
          return converters.convert(entryType, entryType.cast(result));
        } else if (entryType.isPrimitive()) {
          return handlePrimitiveTypes(entryType, result);
        }
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new CSVParseException("Could not invoke getter for field " + entry.getFieldName(), e);
      }
    }
    return converters.getNullValue();
  }

  private String determineGetter(CSVEntry entry) {
    if (boolean.class.isAssignableFrom(entry.getType())) {
      return "is" + StringUtils.capitalize(entry.getFieldName());
    } else {
      return "get" + StringUtils.capitalize(entry.getFieldName());
    }
  }

  private String handlePrimitiveTypes(Class<?> primitiveType, Object boxedObject) {
    if (primitiveType.equals(boolean.class)) {
      return converters.convert(Boolean.class, (Boolean)boxedObject);
    } else if (primitiveType.equals(char.class)) {
      return converters.convert(Character.class, (Character)boxedObject);
    } else if (primitiveType.equals(byte.class)) {
      return converters.convert(Byte.class, (Byte)boxedObject);
    } else if (primitiveType.equals(short.class)) {
      return converters.convert(Short.class, (Short)boxedObject);
    } else if (primitiveType.equals(int.class)) {
      return converters.convert(Integer.class, (Integer)boxedObject);
    } else if (primitiveType.equals(long.class)) {
      return converters.convert(Long.class, (Long)boxedObject);
    } else if (primitiveType.equals(float.class)) {
      return converters.convert(Float.class, (Float)boxedObject);
    } else if (primitiveType.equals(double.class)) {
      return converters.convert(Double.class, (Double)boxedObject);
    }
    return converters.getNullValue();
  }
}
