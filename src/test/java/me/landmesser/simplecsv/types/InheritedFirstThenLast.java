package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVInherit;

@CSVInherit
public class InheritedFirstThenLast extends InheritedBaseLast {
  private long myOwnLong;

  public long getMyOwnLong() {
    return myOwnLong;
  }

  public void setMyOwnLong(long myOwnLong) {
    this.myOwnLong = myOwnLong;
  }
}
