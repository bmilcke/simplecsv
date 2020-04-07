package me.landmesser.simplecsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a strategy to determine which fields to export or import.
 * <p>
 * You can export all fields that are found, or only those that have
 * getters, or both, getters and setters.
 *
 * @see ExportImportStrategy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CSVExportImportStrategy {
  ExportImportStrategy value();
}
