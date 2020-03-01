package me.landmesser.csv.exception;

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
