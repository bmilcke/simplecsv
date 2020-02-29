package me.landmesser.csv;

@FunctionalInterface
public interface ConverterParseFunction<T> {

  T perform(String t) throws CSVConversionException;
}
