package simplecsv.types;

import me.landmesser.simplecsv.CSVConverter;
import me.landmesser.simplecsv.CSVConversionException;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MyDoubleConverter implements CSVConverter<Double> {

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
