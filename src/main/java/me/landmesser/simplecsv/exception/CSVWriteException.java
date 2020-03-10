package me.landmesser.simplecsv.exception;

import me.landmesser.simplecsv.exception.CSVException;

/**
 * Exception thrown when an error while writing the CSV output causes a problem.
 */

public class CSVWriteException extends CSVException {
  public CSVWriteException() {
    super();
  }

  public CSVWriteException(String message) {
    super(message);
  }

  public CSVWriteException(String message, Throwable cause) {
    super(message, cause);
  }

  public CSVWriteException(Throwable cause) {
    super(cause);
  }

  protected CSVWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
