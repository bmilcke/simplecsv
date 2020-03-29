package me.landmesser.simplecsv.util;

public class OrderEntry {
  private final String entryToMove;
  private final String referenceEntry;
  private final OrderDirection direction;

  public OrderEntry(String entryToMove, String referenceEntry, OrderDirection direction) {
    this.entryToMove = entryToMove;
    this.referenceEntry = referenceEntry;
    this.direction = direction;
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
