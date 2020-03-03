package simplecsv;

import me.landmesser.simplecsv.CSVException;
import me.landmesser.simplecsv.CSVParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
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
      // TODO: why does this not work?
//      return StreamSupport.stream(parser.spliterator(), false)
//        .map(this::readSingle);
      List<T> result = new ArrayList<>();
      for(CSVRecord rec : parser) {
        result.add(readSingle(rec));
      }
      return result.stream();
    } catch (IOException e) {
      throw new CSVParseException("Error reading input", e);
    }
  }

  private T readSingle(CSVRecord record) throws CSVParseException {
    try {
      final T result = getType().getConstructor().newInstance();
      Iterator<String> iterator = record.iterator();
      getEntries().forEach(entry -> {
        try {
          if (iterator.hasNext()) {
            evaluate(result, iterator.next(), entry);
          }
        } catch (CSVParseException e) {
          e.printStackTrace();
        }
      });
      return result;
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new CSVParseException("Could not instantiate class " + getType(), e);
    } catch (NoSuchMethodException e) {
      throw new CSVParseException("Class " + getType() + " has no default constructor", e);
    }
  }

  private <R> void evaluate(T targetObject, String value, CSVEntry<R> entry) throws CSVParseException {
    try {
      R converted;
      Class<R> entryType = entry.getType();
      if (entry.getConverter() != null) {
        converted = entry.getConverter().parse(value);
      } else {
        converted = parse(entryType, value);
      }
      // TODO: workaround
      if (converted != null) {
        Method method = getType().getDeclaredMethod(determineSetter(entry), entry.getType());
        method.invoke(targetObject, converted);
      }
    } catch (NoSuchMethodException e) {
      Logger.getLogger(getClass().getSimpleName()).warning(("No setter found for " + entry.getFieldName()));
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new CSVParseException("Could not invoke getter for field " + entry.getFieldName(), e);
    }
  }

}
