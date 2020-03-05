package me.landmesser.simplecsv;

/**
 * Interface for implementing a converter for type <code>T</code>.
 * <p>
 * Conversion in both directions has to be implemented.
 * </p>
 *
 * @param <T> the type which should be converted to a String or
 *            created by parsing an input String.
 */
public interface CSVConverter<T> {

  /**
   * Convert a value to a String representation. It should be lossless, so that
   * back conversion is possible without loss of information. However, this is not
   * required.
   *
   * @param value the value of Type <code>T</code> to convert. May be <code>null</code>
   * @return a String representation e.g. {@link Object#toString()}
   */
  String convert(T value);

  /**
   * Parse a String value to retrieve an object of type <code>T</code>.
   *
   * @param value the String representation coming from a CSV file.
   * @return the converted object.
   * @throws CSVConversionException if there is a problem while parsing
   *                                the input, e.g. if the format is not recognized.
   */
  T parse(String value) throws CSVConversionException;
}
