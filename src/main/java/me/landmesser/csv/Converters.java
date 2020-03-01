package me.landmesser.csv;

import java.util.HashMap;
import java.util.Map;

public class Converters {

  private final Map<Class<?>, CSVConverter<?>> converters = new HashMap<>();

  public <T> void setConverter(Class<T> type, CSVConverter<T> converter) {
    converters.put(type, converter);
  }

  void setUntypedConverter(Class<?> type, CSVConverter<?> converter) {
    converters.put(type, converter);
  }

  public <T> String convert(Class<T> type, T object) {
    if (converters.containsKey(type)) {
      @SuppressWarnings("unchecked")
      String result = ((CSVConverter<T>)(converters.get(type))).convert(object);
      return result;
    } else {
      return object.toString();
    }
  }
}
