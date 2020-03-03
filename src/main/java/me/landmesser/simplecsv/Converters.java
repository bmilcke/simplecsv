package me.landmesser.simplecsv;

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

  @SuppressWarnings("unchecked")
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
