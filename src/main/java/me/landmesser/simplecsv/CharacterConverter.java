package me.landmesser.simplecsv;

public class CharacterConverter extends ToStringConverter<Character> {
  @Override
  public Character parse(String value) throws CSVConversionException {
    return value == null ? null : value.charAt(0);
  }
}
