package me.landmesser.simplecsv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParseAndToStringConverter<T> extends ToStringConverter<T> {

  private final Class<T> type;
  private final Method parseMethod;

  public ParseAndToStringConverter(Class<T> type) throws CSVConversionException {
    this.type = type;
    try {
      // parseXxx methods at String for primitive Wrappers
      parseMethod = type.getDeclaredMethod("parse" + type.getSimpleName(), String.class);
    } catch (NoSuchMethodException e) {
      throw new CSVConversionException("Cannot create converter. Parse method does not exist");
    }
  }

  @SuppressWarnings("unchecked")
  public T parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    try {
      Object result = parseMethod.invoke(null, value);
      if (type.isInstance(result)) {
        return (T) result;
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new CSVConversionException("Could not parse value");
    }
    throw new CSVConversionException("Parse method returned wrong type");
  }
}
