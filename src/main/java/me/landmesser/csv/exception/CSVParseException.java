package me.landmesser.csv.exception;

public class CSVParseException extends Exception {
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
