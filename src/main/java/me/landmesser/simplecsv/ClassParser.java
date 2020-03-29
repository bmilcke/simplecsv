package me.landmesser.simplecsv;

import me.landmesser.simplecsv.util.StringUtils;
import org.apache.commons.csv.CSVFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
class ClassParser<T> {

  private final Conversion conversion = new Conversion();
  private final Class<T> type;
  private final List<FieldEntry> entries;

  private CSVFormat format = CSVFormat.DEFAULT;
  private ColumnNameStyle columnNameStyle = ColumnNameStyle.CAPITALIZED;
  private InheritanceStrategy inheritanceStrategy = InheritanceStrategy.NONE;
  private int inheritanceDepth;

  public ClassParser(Class<T> type) throws CSVException {
    this.type = type;
    entries = parseClass(type);
  }

  private ClassParser(Class<T> type, InheritanceStrategy inheritanceStrategy, int inheritanceDepth) throws CSVException {
    this.type = type;
    this.inheritanceStrategy = inheritanceStrategy;
    this.inheritanceDepth = inheritanceDepth;
    entries = parseClass(type);
  }

  public CSVFormat getFormat() {
    return format;
  }

  protected void setFormat(CSVFormat format) {
    this.format = format;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected List<FieldEntry> parseClass(Class<T> type) throws CSVException {
    detectClassLevelConverters(type);
    determineDefaultColumnStyle(type);
    determineInheritance(type);
    List<FieldEntry> typefieldList = new ArrayList<>();
    Class<? super T> superclass = type.getSuperclass();
    if (inheritanceStrategy == InheritanceStrategy.BASE_FIRST && inheritanceDepth != 0) {
      if (superclass != null && superclass != Object.class) {
        ClassParser parser = new ClassParser(superclass, inheritanceStrategy,
          inheritanceDepth == -1 ? -1 : inheritanceDepth - 1);
        typefieldList.addAll(parser.entries);
      }
    }
    Arrays.stream(type.getDeclaredFields())
      .filter(this::isNotIgnored)
      .map(f -> new FieldEntry(f.getType(), f, columnNameStyle))
      .peek(conversion::fillConverterFor)
      .collect(Collectors.toCollection(() -> typefieldList));
    if (inheritanceStrategy == InheritanceStrategy.BASE_LAST && inheritanceDepth != 0) {
      if (superclass != null && superclass != Object.class) {
        ClassParser parser = new ClassParser(superclass, inheritanceStrategy,
          inheritanceDepth == -1 ? -1 : inheritanceDepth - 1);
        typefieldList.addAll(parser.entries);
      }
    }
    return typefieldList;
  }

  protected String determineSetter(FieldEntry entry) {
    return "set" + StringUtils.capitalize(entry.getFieldName());
  }

  protected String determineGetter(FieldEntry entry) {
    if (boolean.class.isAssignableFrom(entry.getType())) {
      return "is" + StringUtils.capitalize(entry.getFieldName());
    } else {
      return "get" + StringUtils.capitalize(entry.getFieldName());
    }
  }

  public Class<T> getType() {
    return type;
  }

  public List<FieldEntry> getEntries() {
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

  private void detectClassLevelConverters(Class<T> type) throws CSVException {
    List<CSVUseConverter> annotations = Arrays.stream(type.getAnnotationsByType(CSVUseConverter.class))
      .collect(Collectors.toList());
    for (CSVUseConverter anno : annotations) {
      try {
        if (anno.forType() == Void.class) {
          throw new CSVException("Class level annotation requires forType to be set");
        }
        CSVConverter<?> converter = anno.value().getDeclaredConstructor().newInstance();
        conversion.registerUntypedConverter(anno.forType(), converter);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new CSVException("Error setting converter", e);
      }
    }
  }

  private void determineDefaultColumnStyle(Class<T> type) {
    Arrays.stream(type.getAnnotationsByType(CSVDefaultColumnName.class))
      .findAny().map(CSVDefaultColumnName::value).ifPresent(this::setColumnNameStyle);
  }

  private void determineInheritance(Class<T> type) {
    Arrays.stream(type.getAnnotationsByType(CSVInherit.class))
      .findAny().ifPresent(csvInherit -> {
      inheritanceStrategy = csvInherit.value();
      inheritanceDepth = csvInherit.depth();
    });
  }

}
