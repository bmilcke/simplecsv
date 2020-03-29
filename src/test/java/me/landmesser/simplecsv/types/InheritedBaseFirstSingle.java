package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVInherit;

@CSVInherit(depth = 1)
public class InheritedBaseFirstSingle extends Inherited {

  int ownField;

  public int getOwnField() {
    return ownField;
  }

  public void setOwnField(int ownField) {
    this.ownField = ownField;
  }
}
