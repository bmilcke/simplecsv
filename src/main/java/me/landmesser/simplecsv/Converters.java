package me.landmesser.simplecsv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Converters {

  private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS =
    Stream.of(
      Pair.of(boolean.class, Boolean.class),
      Pair.of(byte.class, Byte.class),
      Pair.of(char.class, Character.class),
      Pair.of(double.class, Double.class),
      Pair.of(float.class, Float.class),
      Pair.of(int.class, Integer.class),
      Pair.of(long.class, Long.class),
      Pair.of(short.class, Short.class),
      Pair.of(void.class, Void.class)
    ).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

  private static final Map<Class<? extends TemporalAccessor>, DateTimeFormatter> DATE_TIME_FORMATTERS =
    Stream.of(
      Pair.of(LocalDate.class, DateTimeFormatter.ISO_LOCAL_DATE),
      Pair.of(LocalDateTime.class, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    ).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

  private final Map<Class<?>, CSVConverter<?>> converters = new HashMap<>();

  public <T> void setConverter(Class<T> type, CSVConverter<T> converter) {
    converters.put(type, converter);
  }

  void setUntypedConverter(Class<?> type, CSVConverter<?> converter) {
    converters.put(type, converter);
  }

  CSVConverter<?> converterFor(Class<?> type) {
    return converters.get(type);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public String convert(Class<?> type, Object object) {
    if (object == null) {
      return null;
    }
    if (type.isPrimitive()) {
      return convertPrimitive(type, object);
    }
    if (converters.containsKey(type)) {
      return ((CSVConverter)(converters.get(type))).convert(object);
    } else {
      return object.toString();
    }
  }

  @SuppressWarnings("unchecked, rawtypes")
  public <T> T parse(Class<?> type, String value) throws CSVConversionException {
    if (value == null || value.equals("")) {
      return null;
    }
    if (converters.containsKey(type)) {
      return (T)((CSVConverter)(converters.get(type))).parse(value);
    }
    if (type.isInstance(value)) {
      return (T)type.cast(value);
    }
    try {
      Class<?> adaptedType = type;
      if (type.isPrimitive()) {
        adaptedType = PRIMITIVE_WRAPPERS.get(type);
      } else if (TemporalAccessor.class.isAssignableFrom(type) && DATE_TIME_FORMATTERS.containsKey(type)) {
        TemporalAccessor result = DATE_TIME_FORMATTERS.get(type).parse(value);
        Method fromMethod = type.getDeclaredMethod("from", TemporalAccessor.class);
        Object fromResult = fromMethod.invoke(null, result);
        if (type.isInstance(fromResult)) {
          return (T)fromResult;
        }
      } else if (Enum.class.isAssignableFrom(type)) {
        Method valueOfMethod = type.getDeclaredMethod("valueOf", String.class);
        Object valueOfResult = valueOfMethod.invoke(null, value);
        if (type.isInstance(valueOfResult)) {
          return (T)valueOfResult;
        }
      }
      String parseMethodAppendix = adaptedType.getSimpleName();
      if (parseMethodAppendix.equals("Integer")) {
        parseMethodAppendix = "Int";
      }
      Method valMethod = adaptedType.getDeclaredMethod("parse" + parseMethodAppendix, String.class);
      Object result = valMethod.invoke(null, value);
      if (adaptedType.isInstance(result)) {
        return (T)result;
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new CSVConversionException("No converter found for type " + type.getName(), e);
    } catch(IllegalArgumentException e) {
      throw new CSVConversionException("Value " + value + " could not be parsed for type" + type.getName(), e);
    }
    return null;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  String convertPrimitive(Class<?> primitiveType, Object boxedObject) {
    if (PRIMITIVE_WRAPPERS.containsKey(primitiveType)) {
      Class<?> boxedType = PRIMITIVE_WRAPPERS.get(primitiveType);
      CSVConverter converter = converterFor(boxedType);
      if (converter != null) {
        return converter.convert(boxedObject);
      }
      return boxedObject.toString();
    }
    return null;
  }

  @SuppressWarnings({"rawtypes"})
  <T> T parsePrimitive(Class<T> primitiveType, String input) throws CSVConversionException {
    if (PRIMITIVE_WRAPPERS.containsKey(primitiveType)) {
      Class<?> boxedType = PRIMITIVE_WRAPPERS.get(primitiveType);
      CSVConverter converter = converterFor(boxedType);
      if (converter != null) {
        return primitiveType.cast(converter.parse(input));
      }
    }
    throw new CSVConversionException("Type is not primitive");
  }
}
