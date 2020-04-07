package me.landmesser.simplecsv.types;

import java.time.LocalDate;

public class NoGetterSetter {

  private int noSetterGetter;

  private String noSetter;

  private boolean noBoolSetter;

  private double noGetter;

  private LocalDate fieldToExport;

  public void setNoGetter(double noGetter) {
    this.noGetter = noGetter;
  }

  public String getNoSetter() {
    return noSetter;
  }

  public boolean isNoBoolSetter() {
    return noBoolSetter;
  }

  public LocalDate getFieldToExport() {
    return fieldToExport;
  }

  public void setFieldToExport(LocalDate fieldToExport) {
    this.fieldToExport = fieldToExport;
  }
}
