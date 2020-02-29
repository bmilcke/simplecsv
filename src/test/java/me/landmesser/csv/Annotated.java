package me.landmesser.csv;

import java.time.LocalDate;

public class Annotated {

  @CSVColumnName("Custom")
  private String name;

  @CSVDateFormat("dd.MM.yyyy")
  private LocalDate begin;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBegin() {
    return begin;
  }

  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }
}
