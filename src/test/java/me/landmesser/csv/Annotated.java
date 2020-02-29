package me.landmesser.csv;

import me.landmesser.csv.annotation.CSVColumnName;
import me.landmesser.csv.annotation.CSVConvert;
import me.landmesser.csv.annotation.CSVDateFormat;
import me.landmesser.csv.annotation.CSVIgnore;

import java.time.LocalDate;

public class Annotated {

  @CSVColumnName("Custom")
  private String name;

  @CSVDateFormat("dd.MM.yyyy")
  private LocalDate begin;

  private String unannotated;

  @CSVIgnore
  private String skip;

  @CSVConvert(MyDOubleConverter.class)
  private Double value;

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

  public String getUnannotated() {
    return unannotated;
  }

  public void setUnannotated(String unannotated) {
    this.unannotated = unannotated;
  }

  public String getSkip() {
    return skip;
  }

  public void setSkip(String skip) {
    this.skip = skip;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }
}
