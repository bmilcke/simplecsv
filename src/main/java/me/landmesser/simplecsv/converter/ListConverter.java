package me.landmesser.simplecsv.converter;

import me.landmesser.simplecsv.converter.CSVConversionException;
import me.landmesser.simplecsv.converter.CSVConverter;
import me.landmesser.simplecsv.exception.CSVParseException;
import me.landmesser.simplecsv.exception.CSVWriteException;
import me.landmesser.simplecsv.impl.Converters;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListConverter<T> implements CSVConverter<List<T>> {

  private final char delimStart = '[';
  private final char delimEnd = ']';

  private final Converters conv = new Converters();
  private final Class<T> elemClass;

  public ListConverter(Class<T> elemClass) {
    this.elemClass = elemClass;
  }

  @Override
  public String convert(List<T> value) {
    if (value == null) {
      return null;
    }
    if (value.isEmpty()) {
      return "";
    }
    try (StringWriter sw = new StringWriter();
         CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT)) {
      printer.printRecord(value.stream().map((T obj) -> conv.convert(elemClass, obj)).collect(Collectors.toList()));
      return delimStart + sw.toString().replaceAll("\\r?\\n", "") + delimEnd;
    } catch (IOException ex) {
      throw new CSVWriteException("Could not write collection content");
    }
  }

  @Override
  public List<T> parse(String value) throws CSVConversionException {
    if (value == null) {
      return null;
    }
    if (value.isEmpty()) {
      return Collections.emptyList();
    }
    if (value.charAt(0) != delimStart || value.charAt(value.length() - 1) != delimEnd || value.length() < 2) {
      throw new CSVParseException("List delimiter not found");
    }
    List<T> result = new ArrayList<>();
    try (StringReader sr = new StringReader(value.substring(1, value.length() - 1));
         CSVParser parser = new CSVParser(sr, CSVFormat.DEFAULT)) {
      List<CSVRecord> records = parser.getRecords();
      if (records.size() != 1) {
        throw new CSVParseException("Invalid number of records during parsing list");
      }
      for (String rec : records.get(0)) {
        result.add(conv.parse(elemClass, rec));
      }
      return result;
    } catch (IOException e) {
      throw new CSVParseException("Error during parsing list", e);
    }
  }
}
