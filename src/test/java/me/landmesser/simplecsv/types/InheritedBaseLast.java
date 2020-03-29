package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVInherit;
import me.landmesser.simplecsv.InheritanceStrategy;

@CSVInherit(InheritanceStrategy.BASE_LAST)
public class InheritedBaseLast extends Inherited {

  String ownField;

  public String getOwnField() {
    return ownField;
  }

  public void setOwnField(String ownField) {
    this.ownField = ownField;
  }
}
