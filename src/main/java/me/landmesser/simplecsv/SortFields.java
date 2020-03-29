package me.landmesser.simplecsv;

import java.util.List;
import java.util.stream.Collectors;

public class SortFields implements OrderByField {
  @Override
  public List<String> orderedFields(List<String> fieldNames) {
    return fieldNames.stream().sorted().collect(Collectors.toList());
  }
}
