package me.landmesser.csv;

import me.landmesser.csv.exception.CSVException;
import me.landmesser.csv.exception.CSVParseException;
import me.landmesser.csv.exception.CSVWriteException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked"})
public class CSVWriter<T> extends ClassParser<T> {

  private boolean includeHeaders = true;

  public CSVWriter(Class<T> type) throws CSVException {
    super(type);
  }

  public CSVWriter<T> withFormat(CSVFormat format) {
    setFormat(format);
    return this;
  }

  public CSVWriter<T> withoutHeaders() {
    includeHeaders = false;
    return this;
  }

  public void write(Writer writer, Stream<T> objects) throws CSVWriteException {
    try (CSVPrinter printer = new CSVPrinter(writer,
      includeHeaders ? getFormat().withHeader(retrieveHeaders().toArray(String[]::new)) : getFormat())) {
      // TODO: optimize?
      for (T o : objects.collect(Collectors.toList())) {
        printer.printRecord(retrieveLine(o).toArray());
      }
    } catch (IOException e) {
      throw new CSVWriteException("Error writing CSV file", e);
    }
  }

  private Stream<String> retrieveHeaders() {
    return getEntries().stream()
      .map(CSVEntry::getName);
  }

  private Stream<String> retrieveLine(T object) {
    return getEntries().stream()
      .map(entry -> {
        try {
          return evaluate(object, entry);
        } catch (CSVParseException e) {
          return null;
        }
      });
  }

  private <R> String evaluate(T object, CSVEntry<R> entry) throws CSVParseException {
    if (object != null) {
      try {
        Method method = getType().getDeclaredMethod(determineGetter(entry));
        Object result = method.invoke(object);
        Class<R> entryType = entry.getType();
        if (entry.getConverter() != null) {
          return entry.getConverter().convert(entryType.cast(result));
        }
        return convert(entryType, result);
      } catch (NoSuchMethodException e) {
        Logger.getLogger(getClass().getSimpleName()).warning(("No getter found for " + entry.getFieldName()));
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new CSVParseException("Could not invoke getter for field " + entry.getFieldName(), e);
      }
    }
    return null;
  }
}
