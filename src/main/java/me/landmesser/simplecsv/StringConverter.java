package me.landmesser.simplecsv;

public class StringConverter implements CSVConverter<String> {
  @Override
  public String convert(String value) {
    return value;
  }

  @Override
  public String parse(String value) throws CSVConversionException {
    return value;
  }
}
