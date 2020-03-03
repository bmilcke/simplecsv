package me.landmesser.simplecsv;

import me.landmesser.simplecsv.CSVConversionException;

public interface CSVConverter<T> {

  String convert(T value);

  T parse(String value) throws CSVConversionException;
}
