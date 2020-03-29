package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.Inherited;
import me.landmesser.simplecsv.types.InheritedBaseFirstSingle;
import me.landmesser.simplecsv.types.InheritedBaseLast;
import me.landmesser.simplecsv.types.InheritedFirstThenLast;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InheritanceWriterTest {

  @Test
  void noInheritance() {
    Inherited inherited = new Inherited();
    init(inherited);

    List<String> result = CSVWriterTest.retrieveCSVString(inherited, Inherited.class, null, true);
    assertEquals(2, result.size());
    assertEquals("InheritedString,InheritedBool", result.get(0));
    assertEquals("Inherited,true", result.get(1));
  }

  @Test
  void baseFirstSingle() {
    InheritedBaseFirstSingle inherited = new InheritedBaseFirstSingle();
    init(inherited);
    inherited.setOwnField(17);

    List<String> result = CSVWriterTest.retrieveCSVString(inherited, InheritedBaseFirstSingle.class, null, true);
    assertEquals(2, result.size());
    assertEquals("InheritedString,InheritedBool,OwnField", result.get(0));
    assertEquals("Inherited,true,17", result.get(1));
  }

  @Test
  void baseLast() {
    InheritedBaseLast inherited = new InheritedBaseLast();
    init(inherited);
    inherited.setOwnField("Own");

    List<String> result = CSVWriterTest.retrieveCSVString(inherited, InheritedBaseLast.class, null, true);
    assertEquals(2, result.size());
    assertEquals("OwnField,InheritedString,InheritedBool,BaseString,BaseInt", result.get(0));
    assertEquals("Own,Inherited,true,Base,42", result.get(1));
  }

  @Test
  void firstThenLast() {
    InheritedFirstThenLast inherited = new InheritedFirstThenLast();
    init(inherited);
    inherited.setOwnField("intermediate");
    inherited.setMyOwnLong(20L);

    List<String> result = CSVWriterTest.retrieveCSVString(inherited, InheritedFirstThenLast.class, null, true);
    assertEquals(2, result.size());
    assertEquals("OwnField,InheritedString,InheritedBool,BaseString,BaseInt,MyOwnLong", result.get(0));
    assertEquals("intermediate,Inherited,true,Base,42,20", result.get(1));

  }

  private void init(Inherited inherited) {
    inherited.setBaseInt(42);
    inherited.setBaseString("Base");
    inherited.setInheritedBool(true);
    inherited.setInheritedString("Inherited");
  }
}
