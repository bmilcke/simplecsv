package me.landmesser.csv.types;

import me.landmesser.csv.annotation.CSVConvert;

@CSVConvert(GermanBooleanConverter.class)
public class AnnotatedWithError {

  private boolean field;

  public boolean isField() {
    return field;
  }

  public void setField(boolean field) {
    this.field = field;
  }
}
