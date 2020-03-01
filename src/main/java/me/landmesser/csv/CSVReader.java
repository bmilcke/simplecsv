package me.landmesser.csv;

import me.landmesser.csv.exception.CSVException;
import me.landmesser.csv.exception.CSVParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CSVReader<T> extends ClassParser<T> {

  public CSVReader(Class<T> type) throws CSVException {
    super(type);
  }

  public CSVReader<T> withFormat(CSVFormat format) {
    setFormat(format);
    return this;
  }

  public Stream<T> read(Reader reader) throws CSVParseException {
    try (CSVParser parser = new CSVParser(reader, getFormat())) {
      for (final CSVRecord record : parser) {
        final T result = getType().getConstructor().newInstance();
        Iterator<String> iterator = record.iterator();
        getEntries().forEach(entry -> {
//          try {
            if (iterator.hasNext()) {

//              evaluate(iterator.next(), entry);
            }
//          } catch (CSVParseException e) {
//            e.printStackTrace();
//          }
        });
      }
    } catch (IOException e) {
      throw new CSVParseException("Error reading CSV file", e);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new CSVParseException("Could not instantiate class " + getType(), e);
    } catch (NoSuchMethodException e) {
      throw new CSVParseException("Class " + getType() + " has no default constructor", e);
    }
    return null;
  }

  private <R> void evaluate(T targetObject, R value, CSVEntry<R> entry) throws CSVParseException {
    try {
//      Class<R> entryType = entry.getType();
//      if (entry.getConverter() != null) {
//        return entry.getConverter().apply(entryType.cast(result));
//      }
//      return convert(entryType, result);
      Method method = getType().getDeclaredMethod(determineSetter(entry), entry.getType());
      method.invoke(targetObject, value);
    } catch (NoSuchMethodException e) {
      Logger.getLogger(getClass().getSimpleName()).warning(("No setter found for " + entry.getFieldName()));
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new CSVParseException("Could not invoke getter for field " + entry.getFieldName(), e);
    }
  }

}
