package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.EnumConverter;
import me.landmesser.simplecsv.util.Pair;

import java.util.stream.Stream;

public class TestEnumConverter extends EnumConverter<TestEnum> {

  public TestEnumConverter() {
    super(TestEnum.class, Stream.of(
      Pair.of(TestEnum.FIRST, "One"),
      Pair.of(TestEnum.SECOND, "Two"),
      Pair.of(TestEnum.THIRD, "Three"),
      Pair.of(TestEnum.FOURTH, "Four")
    ));
  }
}
