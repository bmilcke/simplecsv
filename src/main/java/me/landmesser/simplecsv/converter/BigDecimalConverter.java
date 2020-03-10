package me.landmesser.simplecsv.converter;

import java.math.BigDecimal;

public class BigDecimalConverter extends ToStringConverter<BigDecimal> {
  @Override
  public BigDecimal parse(String value) throws CSVConversionException {
    return value == null ? null : new BigDecimal(value);
  }
}
