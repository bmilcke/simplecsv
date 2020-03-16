package me.landmesser.simplecsv;

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

class ListConverter<T> implements CSVConverter<List<T>> {

  private final char startChar;
  private final char endChar;
  private final char delimiter;

  private final CSVConverter<T> elementConverter;
  private final Class<T> elemClass;

  public ListConverter(Class<T> elemClass, CSVConverter<T> elementConverter) {
    this.elemClass = elemClass;
    this.elementConverter = elementConverter;
    this.startChar = '[';
    this.endChar = ']';
    this.delimiter = ',';
  }

  public ListConverter(Class<T> elemClass, CSVConverter<T> elementConverter,
                       final char startChar, final char endChar, final char delimiter) {
    this.elemClass = elemClass;
    this.elementConverter = elementConverter;
    this.startChar = startChar;
    this.endChar = endChar;
    this.delimiter = delimiter;
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
         CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT.withDelimiter(delimiter))) {
      printer.printRecord(value.stream().map(Object::toString).collect(Collectors.toList()));
      return startChar + sw.toString().replaceAll("\\r?\\n", "") + endChar;
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
    if (value.charAt(0) != startChar || value.charAt(value.length() - 1) != endChar || value.length() < 2) {
      throw new CSVParseException("List delimiter not found");
    }
    List<T> result = new ArrayList<>();
    try (StringReader sr = new StringReader(value.substring(1, value.length() - 1));
         CSVParser parser = new CSVParser(sr, CSVFormat.DEFAULT.withDelimiter(delimiter))) {
      List<CSVRecord> records = parser.getRecords();
      if (records.size() != 1) {
        throw new CSVParseException("Invalid number of records during parsing list");
      }
      for (String rec : records.get(0)) {
        result.add(elementConverter.parse(rec));
      }
      return result;
    } catch (IOException e) {
      throw new CSVParseException("Error during parsing list", e);
    }
  }
}
