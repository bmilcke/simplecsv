package me.landmesser.simplecsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets the name of the field in the header.
 * <p>
 * If not present, the field name is converted according to the
 * {@link ColumnNameStyle}, which is {@link ColumnNameStyle#CAPITALIZED} by default.
 * <p>
 * You can change the default by adding the annotation {@link CSVDefaultColumnName}
 * at class level.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CSVColumnName {
  String value();
}
