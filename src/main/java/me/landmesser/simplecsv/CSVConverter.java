package me.landmesser.simplecsv;

public interface CSVConverter<T> {

  String convert(T value);

  T parse(String value) throws CSVConversionException;
}
