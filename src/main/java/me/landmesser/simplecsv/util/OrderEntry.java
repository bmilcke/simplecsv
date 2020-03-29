package me.landmesser.simplecsv.util;

/**
 * Defines a partial ordering of two fields. One field is the reference,
 * the other one is the field to be moved in the given direction.
 */
public class OrderEntry {
  private final String entryToMove;
  private final String referenceEntry;
  private final OrderDirection direction;

  public OrderEntry(String entryToMove, String referenceEntry, OrderDirection direction) {
    this.entryToMove = entryToMove;
    this.referenceEntry = referenceEntry;
    this.direction = direction;
  }

  public static OrderEntry of(String entryToMove, String referenceEntry, OrderDirection direction) {
    return new OrderEntry(entryToMove, referenceEntry, direction);
  }

  public static OrderEntry ofBefore(String entryToMove, String referenceEntry) {
    return new OrderEntry(entryToMove, referenceEntry, OrderDirection.BEFORE);
  }

  public static OrderEntry ofAfter(String entryToMove, String referenceEntry) {
    return new OrderEntry(entryToMove, referenceEntry, OrderDirection.AFTER);
  }

  public String getEntryToMove() {
    return entryToMove;
  }

  public String getReferenceEntry() {
    return referenceEntry;
  }

  public OrderDirection getDirection() {
    return direction;
  }
}
