package me.landmesser.simplecsv.converter;

import me.landmesser.simplecsv.util.Pair;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumConverter<T extends Enum<T>> implements CSVConverter<T> {

  private final Map<T, String> valueMapping;
  private final Class<T> type;

  public EnumConverter(Class<T> type) {
    this.type = type;
    valueMapping = new EnumMap<>(
      Arrays.stream(type.getEnumConstants()).collect(
        Collectors.toMap(Function.identity(), Enum::name)));
  }

  public EnumConverter(Class<T> type, Map<T, String> valueMapping) {
    this(type);
    if (valueMapping != null) {
      this.valueMapping.putAll(valueMapping);
    }
  }

  public EnumConverter(Class<T> type, Stream<Pair<T, String>> mapping) {
    this(type, mapping.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
  }

  @Override
  public String convert(T value) {
    if (value == null) {
      return null;
    }
    return valueMapping.get(value);
  }

  @Override
  public T parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    return valueMapping.entrySet().stream().filter(e -> value.equals(e.getValue()))
      .findFirst().map(Map.Entry::getKey).orElseThrow(CSVConversionException::new);
  }
}
