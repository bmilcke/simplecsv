package me.landmesser.simplecsv;

import me.landmesser.simplecsv.converter.BigDecimalConverter;
import me.landmesser.simplecsv.converter.CSVConversionException;
import me.landmesser.simplecsv.converter.CSVConverter;
import me.landmesser.simplecsv.converter.CharacterConverter;
import me.landmesser.simplecsv.converter.IntegerConverter;
import me.landmesser.simplecsv.converter.TemporalAccessorConverter;
import me.landmesser.simplecsv.converter.UtilDateConverter;
import me.landmesser.simplecsv.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
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

  private final TemporalAccessorConverter taConverter = new TemporalAccessorConverter();

  public Converters() {
    converters.put(Character.class, new CharacterConverter());
    converters.put(Integer.class, new IntegerConverter());
    converters.put(BigDecimal.class, new BigDecimalConverter());
    converters.put(Date.class, new UtilDateConverter());
  }

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
  public <T> T parse(Class<T> type, String value) throws CSVConversionException {
    if (value == null || value.isEmpty()) {
      return null;
    }
    if (type.isInstance(value)) {
      return type.cast(value);
    }
    Class<?> adaptedType = type;
    if (type.isPrimitive()) {
      adaptedType = PRIMITIVE_WRAPPERS.get(type);
    }
    if (converters.containsKey(adaptedType)) {
      return (T)((CSVConverter)(converters.get(adaptedType))).parse(value);
    }
    try {
      if (TemporalAccessor.class.isAssignableFrom(type)) {
        return taConverter.parse(value, type);
      } else if (Enum.class.isAssignableFrom(type)) {
        Method valueOfMethod = type.getDeclaredMethod("valueOf", String.class);
        Object valueOfResult = valueOfMethod.invoke(null, value);
        if (type.isInstance(valueOfResult)) {
          return (T)valueOfResult;
        }
      }
      // parseXxx methods at String for primitive Wrappers
      Method valMethod = adaptedType.getDeclaredMethod("parse" + adaptedType.getSimpleName(), String.class);
      Object result = valMethod.invoke(null, value);
      if (adaptedType.isInstance(result)) {
        return (T)result;
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new CSVConversionException("No converter found for type " + type.getName(), e);
    } catch (IllegalArgumentException e) {
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
}
