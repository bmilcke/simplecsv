package me.landmesser.simplecsv;

import me.landmesser.simplecsv.exception.CSVException;
import me.landmesser.simplecsv.exception.CSVParseException;
import me.landmesser.simplecsv.exception.CSVWriteException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Offers the possibility to write a CSV file to an {@link Appendable}, e.g.
 * a {@link java.io.Writer} by transforming a stream of objects of type <code>T</code>.
 * <p>
 * The class of type <code>T</code> is parsed for annotations to
 * customize the behaviour of the writer.
 * </p>
 *
 * @param <T> the type of objects that serve as input for the csv output.
 */
@SuppressWarnings({"unchecked"})
public class CSVWriter<T> extends ClassParser<T> implements Closeable {

  private final CSVPrinter printer;

  public CSVWriter(Appendable writer, Class<T> type,
    CSVFormat format, boolean withHeaders) throws CSVException, IOException {
    super(type);
    if (format == null) {
      format = CSVFormat.DEFAULT;
    }
    if (withHeaders) {
      format = format.withHeader(retrieveHeaders().toArray(String[]::new));
    }
    this.printer = new CSVPrinter(writer, format);
  }

  public void write(Stream<T> objects) throws CSVWriteException {
    objects.map(this::retrieveLine).map(Stream::toArray)
      .forEach(rec -> {
        try {
          printer.printRecord(rec);
        } catch (IOException e) {
          throw new CSVWriteException("IOException while writing", e);
        }
      });
  }

  @Override
  public void close() throws IOException {
    printer.close();
  }

  private Stream<String> retrieveHeaders() {
    return getEntries().stream()
      .map(CSVEntry::getName);
  }

  private Stream<String> retrieveLine(T object) {
    return getEntries().stream()
      .map(entry -> evaluate(object, entry));
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
