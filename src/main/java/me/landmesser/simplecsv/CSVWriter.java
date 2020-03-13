package me.landmesser.simplecsv;

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

  /**
   * Creates a CSW Writer object with which you can export a stream of objets
   * to an {@link Appendable} in CSV format.
   * <p>
   * This class implements {@link Closeable}, so you can use this class inside a
   * try-with-resource block, like this:
   * </p>
   * <pre>
   *     Stream&lt;MyClass&gt; objects = ...;
   *     try (StringWriter sw = new StringWriter();
   *          CSVWriter&lt;MyClass&gt; writer = new CSVWriter&lt;&gt;(sw, MyClass.class,
   *              CSVFormat.DEFAULT, &#47;* withHeaders *&#47; true )) {
   *       writer.write(objects);
   *       objects.forEach(System.out::println);
   *     } catch (IOException e) {
   *       e.printStackTrace();
   *     }
   * </pre>
   * <p>
   * Note, that you should not call the <code>withHeader</code> methods at the passed
   * {@link CSVFormat} object, as those would compromise the generation of
   * headers by parsing the underlying class <code>T</code>.
   * </p>
   *
   * @param writer      an appendable object to which output will be written
   * @param type        the type of the objects that should be exported. Note that all passed
   *                    objects must have the same class.
   * @param format      the format in which to export. The format object is from apache-commons csv.
   *                    See {@link CSVFormat} for details.
   * @param withHeaders Writes headers as first line when set to <code>true</code>.
   * @throws CSVException if a problem occured creating the underlying apache-commons csv
   *                      {@link CSVPrinter} object.
   */
  public CSVWriter(Appendable writer, Class<T> type,
    CSVFormat format, boolean withHeaders) throws CSVException {
    super(type);
    if (format == null) {
      format = CSVFormat.DEFAULT;
    }
    if (withHeaders) {
      format = format.withHeader(retrieveHeaders().toArray(String[]::new));
    }
    try {
      this.printer = new CSVPrinter(writer, format);
    } catch (IOException e) {
      throw new CSVException("Could not create CSVPrinter", e);
    }
  }

  /**
   * Writes the given stream of objects into the {@link Appendable} given
   * in the constructor.
   *
   * @param objects a stream of objects to write to CSV.
   * @throws CSVWriteException if at some point conversion or writing
   *                           to the {@link Appendable} has problems.
   */
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
      .map(FieldEntry::getName);
  }

  private Stream<String> retrieveLine(T object) {
    return getEntries().stream()
      .map(entry -> evaluate(object, entry));
  }

  private <R> String evaluate(T object, FieldEntry<R> entry) throws CSVParseException {
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
