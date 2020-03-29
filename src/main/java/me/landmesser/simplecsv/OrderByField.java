package me.landmesser.simplecsv;

import java.util.List;

public interface OrderByField {

  List<String> orderedFields(List<String> fieldNames);
}
