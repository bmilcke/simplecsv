package me.landmesser.simplecsv.util;

abstract public class StringUtils {

  public static String capitalize(String in) {
    if (in == null || in.isEmpty()) {
      return in;
    }
    if (in.length() > 1) {
      return in.substring(0, 1).toUpperCase() + in.substring(1);
    } else {
      return in.substring(0, 1).toUpperCase();
    }
  }
}
