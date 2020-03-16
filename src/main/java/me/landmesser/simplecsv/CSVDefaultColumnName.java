package me.landmesser.simplecsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Determines the default style of the generated column header names.
 *
 * @see ColumnNameStyle
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CSVDefaultColumnName {
  ColumnNameStyle value();
}
