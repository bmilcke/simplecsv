package me.landmesser.csv.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Converters {

  public Converters() {
    init();
  }

  private Map<Class<?>, Function<?, String>> converters = new HashMap<>();

  public <T> void setConverter(Class<T> type, Function<T, String> converter) {
    converters.put(type, converter);
  }

  public <T> String convert(Class<T> type, T object) {
    if (converters.containsKey(type)) {
      @SuppressWarnings("unchecked")
      Function<T, String> func = (Function<T, String>)converters.get(type);
      return func.apply(object);
    } else {
      return checkSimpleTypes(object).orElseGet(object::toString);
    }
  }

  private void init() {
    converters.put(Boolean.class, (Boolean b) -> b == null ? "" : (b ? "true" : "false"));
  }

  private <T> Optional<String> checkSimpleTypes(T object) {
    if (boolean.class.isInstance(object)) {
      return Optional.ofNullable(convert(Boolean.class, (Boolean)object));
    }
    return Optional.empty();
  }
}
