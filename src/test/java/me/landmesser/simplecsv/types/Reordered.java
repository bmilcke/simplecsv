package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVColumnName;
import me.landmesser.simplecsv.CSVOrderConstraint;
import me.landmesser.simplecsv.CSVOrderConstraints;

import java.math.BigDecimal;

@CSVOrderConstraints({
  @CSVOrderConstraint(value = "BananaBrand", before = "ZooAnimalCount"),
  @CSVOrderConstraint(value = "CiderPrice", after = "AppleCount"),
  @CSVOrderConstraint(value = "Field", after = "CiderPrice")
})
public class Reordered {

  int zooAnimalCount;
  String bananaBrand;
  @CSVColumnName("Field")
  String renamedField;
  BigDecimal ciderPrice;
  int appleCount;

  public int getZooAnimalCount() {
    return zooAnimalCount;
  }

  public void setZooAnimalCount(int zooAnimalCount) {
    this.zooAnimalCount = zooAnimalCount;
  }

  public String getBananaBrand() {
    return bananaBrand;
  }

  public void setBananaBrand(String bananaBrand) {
    this.bananaBrand = bananaBrand;
  }

  public String getRenamedField() {
    return renamedField;
  }

  public void setRenamedField(String renamedField) {
    this.renamedField = renamedField;
  }

  public BigDecimal getCiderPrice() {
    return ciderPrice;
  }

  public void setCiderPrice(BigDecimal ciderPrice) {
    this.ciderPrice = ciderPrice;
  }

  public int getAppleCount() {
    return appleCount;
  }

  public void setAppleCount(int appleCount) {
    this.appleCount = appleCount;
  }
}
