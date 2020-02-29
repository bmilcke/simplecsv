package me.landmesser.csv;

import me.landmesser.csv.exception.CSVConversionException;

public interface CSVConverter<T> {

  String convert(T value);

  T parse(String value) throws CSVConversionException;
}
