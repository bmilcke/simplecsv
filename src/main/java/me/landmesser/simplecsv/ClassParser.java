package me.landmesser.simplecsv;

import me.landmesser.simplecsv.annotation.CSVDefaultColumnName;
import me.landmesser.simplecsv.annotation.CSVIgnore;
import me.landmesser.simplecsv.annotation.CSVUseConverter;
import me.landmesser.simplecsv.converter.CSVConversionException;
import me.landmesser.simplecsv.exception.CSVException;
import me.landmesser.simplecsv.util.StringUtils;
import org.apache.commons.csv.CSVFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
class ClassParser<T> {

  private final Converters converters = new Converters();
  private final Class<T> type;
  private final List<CSVEntry> entries;

  private CSVFormat format = CSVFormat.DEFAULT;
  private ColumnNameStyle columnNameStyle = ColumnNameStyle.CAPITALIZED;

  public ClassParser(Class<T> type) throws CSVException {
    this.type = type;
    entries = parseClass(type);
  }

  public CSVFormat getFormat() {
    return format;
  }

  protected void setFormat(CSVFormat format) {
    this.format = format;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected List<CSVEntry> parseClass(Class<T> type) throws CSVException {
    detectConverters(type);
    determineDefaultColumnStyle(type);
    return Arrays.stream(type.getDeclaredFields())
      .filter(this::isNotIgnored)
      .map(f -> new CSVEntry(f.getType(), f, columnNameStyle))
      .collect(Collectors.toList());
  }

  protected String convert(Class<?> type, Object object) {
    return converters.convert(type, object);
  }

  protected <R> R parse(Class<R> type, String value) throws CSVConversionException {
    return converters.parse(type, value);
  }

  protected String determineSetter(CSVEntry entry) {
    return "set" + StringUtils.capitalize(entry.getFieldName());
  }

  protected String determineGetter(CSVEntry entry) {
    if (boolean.class.isAssignableFrom(entry.getType())) {
      return "is" + StringUtils.capitalize(entry.getFieldName());
    } else {
      return "get" + StringUtils.capitalize(entry.getFieldName());
    }
  }

  public Class<T> getType() {
    return type;
  }

  public List<CSVEntry> getEntries() {
    return entries;
  }

  public ColumnNameStyle getColumnNameStyle() {
    return columnNameStyle;
  }

  private void setColumnNameStyle(ColumnNameStyle columnNameStyle) {
    this.columnNameStyle = columnNameStyle;
  }

  private boolean isNotIgnored(Field field) {
    return !Arrays.stream(field.getAnnotationsByType(CSVIgnore.class))
      .findAny().isPresent();
  }

  private void detectConverters(Class<T> type) throws CSVException {
    List<CSVUseConverter> annotations = Arrays.stream(type.getAnnotationsByType(CSVUseConverter.class))
      .collect(Collectors.toList());
    for (CSVUseConverter anno : annotations) {
      try {
        if (anno.forType() == Void.class) {
          throw new CSVException("Class level annotation requires forType to be set");
        }
        converters.setUntypedConverter(anno.forType(),
          anno.value().getDeclaredConstructor().newInstance());
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new CSVException("Error setting converter", e);
      }
    }
  }

  private void determineDefaultColumnStyle(Class<T> type) {
    Arrays.stream(type.getAnnotationsByType(CSVDefaultColumnName.class))
      .findAny().map(CSVDefaultColumnName::value).ifPresent(this::setColumnNameStyle);

  }
}
