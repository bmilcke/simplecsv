package me.landmesser.simplecsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells the {@link CSVWriter}  or {@link CSVReader} to use the given format for any
 * date or date/time format derived from {@link java.time.temporal.TemporalAccessor}
 * implementing a method <code>format()</code> for conversion to String, and
 * <code>parse()</code> for parsing a String back into a date.
 * <p>
 * An old-school {@link java.util.Date} is also supported.
 *
 * <p>
 * For the formatting syntax see {@link java.time.format.DateTimeFormatter}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CSVDateFormat {
  String value();
}
