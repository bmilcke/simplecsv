package me.landmesser.csv;

import me.landmesser.csv.exception.CSVConversionException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Converters {

  private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS =
    Stream.of(
      new ImmutablePair<>(boolean.class, Boolean.class),
      new ImmutablePair<>(byte.class, Byte.class),
      new ImmutablePair<>(char.class, Character.class),
      new ImmutablePair<>(double.class, Double.class),
      new ImmutablePair<>(float.class, Float.class),
      new ImmutablePair<>(int.class, Integer.class),
      new ImmutablePair<>(long.class, Long.class),
      new ImmutablePair<>(short.class, Short.class),
      new ImmutablePair<>(void.class, Void.class)
    ).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

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

  public <T> T parse(Class<?> type, String value) throws CSVConversionException {
    if (value != null && value.equals("")) {
      return null;
    }
    if (type.isInstance(value)) {
      return (T)type.cast(value);
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
