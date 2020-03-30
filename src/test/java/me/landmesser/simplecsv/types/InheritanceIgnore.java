package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.CSVInherit;
import me.landmesser.simplecsv.InheritanceStrategy;

@CSVInherit(value = InheritanceStrategy.BASE_FIRST, ignore = "MyOwnInt,End,Unannotated,Old Date,NotPresent,DateList")
public class InheritanceIgnore extends Annotated {
  int myOwnInt;
  String myOwnString;

  public int getMyOwnInt() {
    return myOwnInt;
  }

  public void setMyOwnInt(int myOwnInt) {
    this.myOwnInt = myOwnInt;
  }

  public String getMyOwnString() {
    return myOwnString;
  }

  public void setMyOwnString(String myOwnString) {
    this.myOwnString = myOwnString;
  }
}
