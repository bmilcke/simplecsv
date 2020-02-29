package me.landmesser.csv;

public class CSVConversionException extends Exception {
  public CSVConversionException() {
    super();
  }

  public CSVConversionException(String message) {
    super(message);
  }

  public CSVConversionException(String message, Throwable cause) {
    super(message, cause);
  }

  public CSVConversionException(Throwable cause) {
    super(cause);
  }

  protected CSVConversionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
