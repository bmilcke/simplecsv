package me.landmesser.simplecsv;

import java.util.List;

/**
 * Interface for providing Classes that allow
 * ordering a list of Strings. This is done by
 * returning the given list in the desired order.
 */
@FunctionalInterface
public interface FieldOrder {

  /**
   * Re-order the given list of fields (Strings). This is used by
   * the {@link CSVWriter} as well as the {@link CSVReader} to determine
   * a different order than the one given by the declaration in the class
   * used as basis of the CSV-export or -import.
   *
   * @param fieldNames a list of field names in the order they would be used
   *                   if no ordering was present.
   * @return the re-ordered list of field names.
   */
  List<String> orderedFields(List<String> fieldNames);
}
