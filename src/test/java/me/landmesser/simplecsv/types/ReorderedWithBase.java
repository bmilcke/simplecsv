package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVInherit;
import me.landmesser.simplecsv.CSVOrderConstraint;
import me.landmesser.simplecsv.InheritanceStrategy;

@CSVInherit(InheritanceStrategy.BASE_FIRST)
@CSVOrderConstraint(value = "BaseString", after = "AppleCount")
public class ReorderedWithBase extends Base {

  int appleCount;
  int pearCount;

  public int getAppleCount() {
    return appleCount;
  }

  public void setAppleCount(int appleCount) {
    this.appleCount = appleCount;
  }

  public int getPearCount() {
    return pearCount;
  }

  public void setPearCount(int pearCount) {
    this.pearCount = pearCount;
  }
}
