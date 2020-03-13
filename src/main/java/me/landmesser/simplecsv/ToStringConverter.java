package me.landmesser.simplecsv;

abstract class ToStringConverter<T> implements CSVConverter<T> {
  @Override
  public String convert(T value) {
    return value == null ? null : value.toString();
  }
}
