package me.landmesser.simplecsv;

/**
 * Determines the default column name style.
 *
 * @see CSVDefaultColumnName
 */
public enum ColumnNameStyle {

  /**
   * The column name is exactly the same as the field name
   */
  LIKE_FIELD,
  /**
   * The column name is the same as the field name, except
   * the first character, which is upper case
   */
  CAPITALIZED,
  /**
   * The column name is the field name in upper case letters
   */
  UPPERCASE,
  /**
   * The column name is the field name in lower case letters
   */
  LOWERCASE
}
