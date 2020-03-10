package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.annotation.CSVUseConverter;

@CSVUseConverter(GermanBooleanConverter.class)
public class AnnotatedWithError {

  private boolean field;

  public boolean isField() {
    return field;
  }

  public void setField(boolean field) {
    this.field = field;
  }
}
