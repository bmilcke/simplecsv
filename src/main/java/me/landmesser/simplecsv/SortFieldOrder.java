package me.landmesser.simplecsv;

import java.util.List;
import java.util.stream.Collectors;

public class SortFieldOrder implements FieldOrder {
  @Override
  public List<String> orderedFields(List<String> fieldNames) {
    return fieldNames.stream().sorted().collect(Collectors.toList());
  }
}
