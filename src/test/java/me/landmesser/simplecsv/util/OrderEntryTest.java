package me.landmesser.simplecsv.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntryTest {

  @Test
  void of() {
    OrderEntry entry = OrderEntry.of("foo", "bar", OrderDirection.AFTER);
    assertEquals("foo", entry.getEntryToMove());
    assertEquals("bar", entry.getReferenceEntry());
    assertEquals(OrderDirection.AFTER, entry.getDirection());

    entry = OrderEntry.of("foo", "bar", OrderDirection.BEFORE);
    assertEquals("foo", entry.getEntryToMove());
    assertEquals("bar", entry.getReferenceEntry());
    assertEquals(OrderDirection.BEFORE, entry.getDirection());
  }

  @Test
  void ofBefore() {
    OrderEntry entry = OrderEntry.ofBefore("foo", "bar");
    assertEquals("foo", entry.getEntryToMove());
    assertEquals("bar", entry.getReferenceEntry());
    assertEquals(OrderDirection.BEFORE, entry.getDirection());
  }

  @Test
  void ofAfter() {
    OrderEntry entry = OrderEntry.ofAfter("foo", "bar");
    assertEquals("foo", entry.getEntryToMove());
    assertEquals("bar", entry.getReferenceEntry());
    assertEquals(OrderDirection.AFTER, entry.getDirection());
  }
}