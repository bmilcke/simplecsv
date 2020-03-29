package me.landmesser.simplecsv.types;

public class Inherited extends Base {

  private String inheritedString;

  private Boolean inheritedBool;

  public String getInheritedString() {
    return inheritedString;
  }

  public void setInheritedString(String inheritedString) {
    this.inheritedString = inheritedString;
  }

  public Boolean getInheritedBool() {
    return inheritedBool;
  }

  public void setInheritedBool(Boolean inheritedBool) {
    this.inheritedBool = inheritedBool;
  }
}
