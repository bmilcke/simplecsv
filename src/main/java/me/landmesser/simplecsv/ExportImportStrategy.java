package me.landmesser.simplecsv;

/**
 * Determine which fields to export concerning the
 * existence of getters and setters.
 */
public enum ExportImportStrategy {
  /**
   * Export and import all fields found in the class.
   */
  ALL,
  /**
   * Export and import only those fields that have
   * a valid getter.
   */
  WITH_GETTERS,
  /**
   * Export and import only those fields that have
   * both, a valid getter and a valid setter.
   */
  WITH_GETTERS_AND_SETTERS
}
