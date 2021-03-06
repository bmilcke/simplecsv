package me.landmesser.simplecsv;

/**
 * Base class for all runtime exceptions thrown by this module.
 */
public class CSVException extends RuntimeException {
  public CSVException() {
    super();
  }

  public CSVException(String message) {
    super(message);
  }

  public CSVException(String message, Throwable cause) {
    super(message, cause);
  }

  public CSVException(Throwable cause) {
    super(cause);
  }

  protected CSVException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
