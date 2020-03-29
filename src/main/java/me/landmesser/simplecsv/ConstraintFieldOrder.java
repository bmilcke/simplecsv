package me.landmesser.simplecsv;

import me.landmesser.simplecsv.util.OrderEntry;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Provides a simple facility for ordering fields.
 * <p>
 * The order entries given in the constructor are applied one after the other.
 * This means that a field can move multiple times, and references are relative
 * to the state when being applied.
 * <p>
 * For example if you have <code>"A", "B", "C"</code> and put <code>"C"</code>
 * before <code>"A"</code> and then <code>"B"</code> before <code>"C"</code>,
 * you will get <code>"B", "C", "A"</code>.
 */
public class ConstraintFieldOrder implements FieldOrder {

  private final List<OrderEntry> constraints;

  public ConstraintFieldOrder(List<OrderEntry> constraints) {
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
