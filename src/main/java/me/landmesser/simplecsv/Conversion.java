package me.landmesser.simplecsv;

import me.landmesser.simplecsv.util.Pair;

import java.math.BigDecimal;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked,rawtypes")
class Conversion {

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

  public Conversion() {
    converters.put(Character.class, new CharacterConverter());
    converters.put(Integer.class, new IntegerConverter());
    converters.put(BigDecimal.class, new BigDecimalConverter());
    converters.put(Date.class, new UtilDateConverter());
    converters.put(String.class, new StringConverter());
  }

  /**
   * for setting custom class-level converters
   *
   * @param type      the type which should be converted by the converter
   * @param converter the converter itself
   */
  void registerUntypedConverter(Class<?> type, CSVConverter<?> converter) {
    converters.put(type, converter);
  }

  /**
   * Note: making FieldEntry generic breaks the calling code with Java 8.
   */
  public void fillConverterFor(FieldEntry entry) {
    // custom converter already set at entry
    if (entry.getConverter() != null) {
      return;
    }

    Class entryType = entry.getType();
    if (!converters.containsKey(entryType)) {
      converters.put(entryType, getConverterForType(entryType, entry.getGenericTypes()));
    }
    if (converters.containsKey(entryType)) {
      entry.setConverter(converters.get(entryType));
    } else {
      throw new CSVException("No converter found for type " + entryType.getName());
    }
  }

  public <T> CSVConverter<T> getConverterForType(Class<T> type, List<Class> genericTypes) {
    if (converters.containsKey(type)) {
      return (CSVConverter<T>) converters.get(type);
    }

    // use wrapper classes for primitives
    if (type.isPrimitive()) {
      Class<?> wrappedType = PRIMITIVE_WRAPPERS.get(type);
      if (!converters.containsKey(wrappedType)) {
        converters.put(wrappedType, new ParseAndToStringConverter(wrappedType));
      }
      return (CSVConverter) converters.get(wrappedType);
    }

    if (PRIMITIVE_WRAPPERS.containsValue(type)) {
      return new ParseAndToStringConverter<>(type);
    }
    if (Enum.class.isAssignableFrom(type)) {
      return new EnumConverter(type);
    }
    if (TemporalAccessor.class.isAssignableFrom(type)) {
      return new TemporalAccessorConverter(type);
    }
    if (List.class.isAssignableFrom(type) && !genericTypes.isEmpty()) {
      return new ListConverter(getConverterForType(genericTypes.get(0), Collections.emptyList()));
    }
    return null;
  }
}
