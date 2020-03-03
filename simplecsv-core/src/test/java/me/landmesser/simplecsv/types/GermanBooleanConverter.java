package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVConverter;
import me.landmesser.simplecsv.CSVConversionException;

public class GermanBooleanConverter implements CSVConverter<Boolean> {

  private static final String TRUE_VAL = "wahr";
  private static final String FALSE_VAL = "falsch";
  @Override
  public String convert(Boolean value) {
    return value == null ? null : value ? TRUE_VAL : FALSE_VAL;
  }

  @Override
  public Boolean parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    } else if (value.equals(TRUE_VAL)) {
      return true;
    } else if (value.equals(FALSE_VAL)) {
      return false;
    } else {
      throw new CSVConversionException("Unknown Value for Boolean");
    }
  }
}
