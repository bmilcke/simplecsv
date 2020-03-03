package me.landmesser.simplecsv;

import me.landmesser.simplecsv.CSVConversionException;

@FunctionalInterface
public interface ConverterParseFunction<T> {

  T perform(String t) throws CSVConversionException;
}
