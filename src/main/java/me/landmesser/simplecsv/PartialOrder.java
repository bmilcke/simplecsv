package me.landmesser.simplecsv;

import me.landmesser.simplecsv.util.OrderEntry;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PartialOrder implements OrderByField {

  private final List<OrderEntry> constraints;

  public PartialOrder(List<OrderEntry> constraints) {
    this.constraints = Objects.requireNonNull(constraints);
  }

  @Override
  public List<String> orderedFields(List<String> fieldNames) {
    constraints.forEach(constraint -> applyConstraint(constraint, fieldNames));
    return fieldNames;
  }

  private void applyConstraint(OrderEntry entry, List<String> fieldNames) {
    int refIndex = fieldNames.indexOf(entry.getReferenceEntry());
    if (refIndex == -1) {
      throw new CSVException("Reference entry " + entry.getReferenceEntry() + " not found.");
    }
    int elemIndex = fieldNames.indexOf(entry.getEntryToMove());
    if (elemIndex == -1) {
      throw new CSVException("Entry " + entry.getEntryToMove() + " not found.");
    }
    if (refIndex == elemIndex) {
      throw new CSVException("Entry cannot be moved before or after itself");
    }
    if (refIndex > elemIndex) {
      switch (entry.getDirection()) {
        case BEFORE:
          Collections.rotate(fieldNames.subList(elemIndex, refIndex), -1);
          break;
        case AFTER:
          Collections.rotate(fieldNames.subList(elemIndex, refIndex + 1), -1);
          break;
      }
    } else {
      switch (entry.getDirection()) {
        case BEFORE:
          Collections.rotate(fieldNames.subList(refIndex, elemIndex + 1), 1);
          break;
        case AFTER:
          Collections.rotate(fieldNames.subList(refIndex + 1, elemIndex + 1), 1);
          break;
      }
    }
  }
}
