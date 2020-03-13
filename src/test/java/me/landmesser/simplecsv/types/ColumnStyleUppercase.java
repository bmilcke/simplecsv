package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.ColumnNameStyle;
import me.landmesser.simplecsv.CSVDefaultColumnName;

@CSVDefaultColumnName(ColumnNameStyle.UPPERCASE)
public class ColumnStyleUppercase {

  private String first;
  private String second;
  private String anotherOne;

  public String getFirst() {
    return first;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  public String getSecond() {
    return second;
  }

  public void setSecond(String second) {
    this.second = second;
  }

  public String getAnotherOne() {
    return anotherOne;
  }

  public void setAnotherOne(String anotherOne) {
    this.anotherOne = anotherOne;
  }
}
