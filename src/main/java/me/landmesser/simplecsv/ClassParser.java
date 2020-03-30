package me.landmesser.simplecsv;

import me.landmesser.simplecsv.util.OrderDirection;
import me.landmesser.simplecsv.util.OrderEntry;
import me.landmesser.simplecsv.util.StringUtils;
import org.apache.commons.csv.CSVFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
  private Set<String> baseIgnoreFields = Collections.emptySet();

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
    if (inheritanceStrategy == InheritanceStrategy.BASE_FIRST && inheritanceDepth != 0) {
      handleInheritance(type.getSuperclass(), typefieldList);
    }
    Arrays.stream(type.getDeclaredFields())
      .filter(this::isNotIgnored)
      .map(f -> new FieldEntry(f.getType(), f, columnNameStyle))
      .peek(conversion::fillConverterFor)
      .collect(Collectors.toCollection(() -> typefieldList));
    if (inheritanceStrategy == InheritanceStrategy.BASE_LAST && inheritanceDepth != 0) {
      handleInheritance(type.getSuperclass(), typefieldList);
    }
    handleOrderByField(typefieldList);
    handleOrderConstraints(typefieldList);
    return typefieldList;
  }

  private void handleInheritance(Class<? super T> superclass, List<FieldEntry> typefieldList) {
    if (superclass != null && superclass != Object.class) {
      ClassParser parser = new ClassParser(superclass, inheritanceStrategy,
        inheritanceDepth == -1 ? -1 : inheritanceDepth - 1);
      if (!baseIgnoreFields.isEmpty()) {
        ((List<FieldEntry>) parser.entries).stream()
          .filter(e -> !baseIgnoreFields.contains(e.getName()))
          .collect(Collectors.toCollection(() -> typefieldList));
      } else {
        typefieldList.addAll(parser.entries);
      }
    }
  }

  private void handleOrderByField(List<FieldEntry> typefieldList) {
    Arrays.stream(type.getAnnotationsByType(CSVOrderFields.class))
      .findAny().map(CSVOrderFields::value)
      .map(clazz -> {
        try {
          return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          return null;
        }
      })
      .ifPresent(orderByField -> orderFields(typefieldList, orderByField));
  }

  private void orderFields(List<FieldEntry> typefieldList, FieldOrder fieldOrder) {
    final List<String> nameList = typefieldList.stream()
      .map(FieldEntry::getName)
      .collect(Collectors.toList());
    final List<String> ordered = fieldOrder.orderedFields(nameList);
    final Map<String, FieldEntry> entriesByKey = typefieldList.stream()
      .collect(Collectors.toMap(FieldEntry::getName, Function.identity()));
    if (!entriesByKey.keySet().equals(new HashSet<>(ordered))) {
      throw new CSVException("Ordered Fields are incorrect");
    }
    typefieldList.clear();
    typefieldList.addAll(ordered.stream().map(entriesByKey::get).collect(Collectors.toList()));
  }

  private void handleOrderConstraints(List<FieldEntry> typefieldList) {
    List<OrderEntry> orderEntryList = Arrays.stream(type.getAnnotationsByType(CSVOrderConstraint.class))
      .map(this::orderEntryByAnno)
      .collect(Collectors.toList());
    if (!orderEntryList.isEmpty()) {
      orderFields(typefieldList, new ConstraintFieldOrder(orderEntryList));
    }
  }

  private OrderEntry orderEntryByAnno(CSVOrderConstraint constraint) {
    if (!"".equals(constraint.before())) {
      return OrderEntry.ofBefore(constraint.value(), constraint.before());
    }
    if (!"".equals(constraint.after())) {
      return OrderEntry.ofAfter(constraint.value(), constraint.after());
    }
    throw new CSVException("CSVOrderConstraint must have set either before or after");
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
      baseIgnoreFields = Arrays.stream(csvInherit.ignore().split(",")).collect(Collectors.toSet());
    });
  }
}
