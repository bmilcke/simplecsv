package me.landmesser.csv.exception;

public class CSVWriteException extends Exception{
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
