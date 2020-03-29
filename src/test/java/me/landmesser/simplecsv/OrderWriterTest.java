package me.landmesser.simplecsv;

import me.landmesser.simplecsv.types.Reordered;
import me.landmesser.simplecsv.types.ReorderedWithBase;
import me.landmesser.simplecsv.types.Sorted;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderWriterTest {

  @Test
  void sorted() {
    Sorted object = new Sorted();
    object.setAppleCount(2);
    object.setZooAnimalCount(5);

    List<String> result = CSVWriterTest.retrieveCSVString(object, Sorted.class, null, true);
    assertEquals(2, result.size());

    assertEquals("AppleCount,BananaBrand,CiderPrice,Field,ZooAnimalCount", result.get(0));
    assertEquals("2,,,,5", result.get(1));
  }

  @Test
  void reordered() {
    Reordered object = new Reordered();
    object.setCiderPrice(BigDecimal.valueOf(2560, 2));
    object.setBananaBrand("MyBanana");
    object.setZooAnimalCount(216);

    List<String> result = CSVWriterTest.retrieveCSVString(object, Reordered.class, null, true);
    assertEquals(2, result.size());

    assertEquals("BananaBrand,ZooAnimalCount,AppleCount,CiderPrice,Field", result.get(0));
    assertEquals("MyBanana,216,0,25.60,", result.get(1));
  }

  @Test
  void reorderedWithBase() {
    ReorderedWithBase object = new ReorderedWithBase();
    object.setBaseString("base");
    object.setBaseInt(1);
    object.setAppleCount(2);
    object.setPearCount(3);

    List<String> result = CSVWriterTest.retrieveCSVString(object, ReorderedWithBase.class, null, true);
    assertEquals(2, result.size());
    assertEquals("BaseInt,AppleCount,BaseString,PearCount", result.get(0));
    assertEquals("1,2,base,3", result.get(1));

  }
}
