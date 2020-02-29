package me.landmesser.csv;

import me.landmesser.csv.annotation.CSVIgnore;
import me.landmesser.csv.exception.CSVParseException;
import me.landmesser.csv.exception.CSVWriteException;
import me.landmesser.csv.impl.CSVEntry;
import me.landmesser.csv.impl.Converters;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CSVWriter<T> {

  private final Class<T> type;
  private final List<CSVEntry> entries;
  private Converters converters = new Converters();

  private CSVFormat format = CSVFormat.DEFAULT;

  public CSVWriter(Class<T> type) {
    this.type = type;
    entries = Arrays.stream(type.getDeclaredFields())
      .filter(this::isNotIgnored)
      .map(f -> new CSVEntry(f.getType(), f))
      .collect(Collectors.toList());
  }

  public CSVWriter<T> withFormat(CSVFormat format) {
    this.format = format;
    return this;
  }

  public void write(Writer writer, Stream<T> objects) throws CSVWriteException {
    try (CSVPrinter printer = new CSVPrinter(writer, format.withHeader(
      retrieveHeaders().toArray(String[]::new)))) {
      // TODO: optimize?
      for (T o : objects.collect(Collectors.toList())) {
        printer.printRecord(retrieveLine(o).toArray());
      }
    } catch (IOException e) {
      throw new CSVWriteException("Error writing CSV file", e);
    }
  }

  private Stream<String> retrieveHeaders() {
    return entries.stream()
      .map(CSVEntry::getName);
  }

  private Stream<String> retrieveLine(T object) {
    return entries.stream()
      .map(entry -> {
        try {
          return evaluate(object, entry);
        } catch (CSVParseException e) {
          return "";
        }
      });
  }

  private boolean isNotIgnored(Field field) {
    return !Arrays.stream(field.getAnnotationsByType(CSVIgnore.class))
      .findAny().isPresent();
  }

  private <R> String evaluate(T object, CSVEntry<R> entry) throws CSVParseException {
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
