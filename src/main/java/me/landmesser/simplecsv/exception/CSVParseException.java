package me.landmesser.simplecsv.exception;

/**
 * Exception thrown when an error while parsing the CSV input causes a problem.
 */
public class CSVParseException extends CSVException {
  public CSVParseException() {
    super();
  }

  public CSVParseException(String message) {
    super(message);
  }

  public CSVParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public CSVParseException(Throwable cause) {
    super(cause);
  }

  protected CSVParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
