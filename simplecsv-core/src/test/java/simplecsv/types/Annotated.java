package simplecsv.types;

import me.landmesser.simplecsv.CSVColumnName;
import me.landmesser.simplecsv.CSVUseConverter;
import me.landmesser.simplecsv.CSVUseConverters;
import me.landmesser.simplecsv.CSVDateFormat;
import me.landmesser.simplecsv.CSVIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;

@CSVUseConverters({
  @CSVUseConverter(value = GermanBooleanConverter.class, forType = Boolean.class),
  @CSVUseConverter(value = MyLocalDateTimeConverter.class, forType = LocalDateTime.class)
})
public class Annotated {

  @CSVColumnName("Custom")
  private String name;

  @CSVDateFormat("dd.MM.yyyy")
  private LocalDate begin;

  private LocalDateTime end;

  private String unannotated;

  @CSVIgnore
  private String skip;

  @CSVUseConverter(MyDoubleConverter.class)
  private Double value;

  // Converter set at class level should be used
  private boolean checked;

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

  public LocalDateTime getEnd() {
    return end;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
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

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }
}
