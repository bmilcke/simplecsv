package me.landmesser.csv;

import me.landmesser.csv.exception.CSVConversionException;

public class GermanBooleanConverter implements CSVConverter<Boolean> {
  @Override
  public String convert(Boolean value) {
    return value == null ? null : value ? "wahr" : "falsch";
  }

  @Override
  public Boolean parse(String value) throws CSVConversionException {
    return value == null ? null : value.equals("Wahr");
  }
}
