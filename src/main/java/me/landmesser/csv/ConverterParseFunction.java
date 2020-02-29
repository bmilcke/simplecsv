package me.landmesser.csv;

import me.landmesser.csv.exception.CSVConversionException;

@FunctionalInterface
public interface ConverterParseFunction<T> {

  T perform(String t) throws CSVConversionException;
}
