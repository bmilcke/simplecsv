package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.annotation.CSVUseConverter;

public class EnumConverterContainer {

  @CSVUseConverter(TestEnumConverter.class)
  private TestEnum myEnum;

  public TestEnum getMyEnum() {
    return myEnum;
  }

  public void setMyEnum(TestEnum myEnum) {
    this.myEnum = myEnum;
  }
}
