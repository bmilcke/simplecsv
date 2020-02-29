package me.landmesser.csv;

import me.landmesser.csv.exception.CSVConversionException;
import org.junit.platform.commons.util.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MyDOubleConverter implements CSVConverter<Double> {

  @Override
  public String convert(Double value) {
    if (value == null) {
      return "";
    }
    NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);
    formatter.setMaximumFractionDigits(2);
    return formatter.format(value);
  }

  @Override
  public Double parse(String value) throws CSVConversionException {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    try {
      return NumberFormat.getInstance(Locale.GERMAN).parse(value).doubleValue();
    } catch (ParseException e) {
      throw new CSVConversionException("Cannot parse " + value + " as Double", e);
    }
  }
}
