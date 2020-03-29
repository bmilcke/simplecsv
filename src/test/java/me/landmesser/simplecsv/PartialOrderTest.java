package me.landmesser.simplecsv;

import me.landmesser.simplecsv.util.OrderDirection;
import me.landmesser.simplecsv.util.OrderEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PartialOrderTest {

  @Test
  void orderedFields() {
    List<OrderEntry> orderEntries = new ArrayList<>();

    List<String> fields = Stream.of("A", "B", "C", "D", "E", "F").collect(Collectors.toList());
    orderEntries.add(OrderEntry.of("C", "A", OrderDirection.BEFORE));
    orderEntries.add(OrderEntry.of("D", "F", OrderDirection.BEFORE));
    orderEntries.add(OrderEntry.of("E", "C", OrderDirection.AFTER));
    orderEntries.add(OrderEntry.of("A", "F", OrderDirection.AFTER));
    orderEntries.add(OrderEntry.of("D", "B", OrderDirection.BEFORE));
    List<String> expected = Stream.of("C", "E", "D", "B", "F", "A").collect(Collectors.toList());

    ConstraintFieldOrder order = new ConstraintFieldOrder(orderEntries);

    List<String> result = order.orderedFields(fields);
    assertEquals(expected, result);
  }
}