# simplecsv: _Java Library for exporting Objects to CSV_

[![Latest release](https://img.shields.io/github/release/bmilcke/simplecsv.svg)](https://github.com/bmilcke/simplecsv/releases/latest)
![Java CI](https://github.com/bmilcke/simplecsv/workflows/Java%20CI/badge.svg)

The goal of this library is to enable you to create and parse CSV-files (or streams) easily. CSV is a simple
data format which puts values into a line of text separated by a delimiter, which is typically a comma or
semicolon. Thus the name CSV for **C**omma **S**eparated **V**alues. (see [Wikipedia][2])

This library bases upon [Commons CSV from Apache Commons][1]. In addition to the functionality of commons CSV, with
simplecsv you can create DTO-like objects that describe the fields you want to export to CSV. 

## Adding simplecsv to your build

You can add this library to your Maven-based project by adding this dependency to the `dependencies`-section 
of your `pom.xml`.

```xml
<dependency>
  <groupId>me.landmesser</groupId>
  <artifactId>simplecsv</artifactId>
  <version>0.4</version>
</dependency>
```

## Example

Imagine, you want to export a CSV-file containing the fields `firstname`, `lastname`, `dateOfBirth` of a
bunch of people in a database. You might have a larger entity object containig these fields among others.

```java
@Entity
public class Person {
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String customerId;
  // more fields
  // ...
  // getters and setters
  // ...
}
```

Then you can write a DTO-object containig the fields you want to export to CSV.

```java
public class CsvEntity {
  private String firstname;
  private String lastname;
  @CSVColumnName("Date of Birth")
  @CSVDateFormat("dd.MM.yyyy")
  private LocalDate dateOfBirth;
  // getters and setters
}
```

In this simple case, you can just copy the three fields from the entity to the DTO. However, if you have more fields
it might help to use tools like [Mapstruct][3] to map the fields. You can then just export this DTO by using the
following code. (Here a StringWriter is used for simplicity, but of course you will typically use some sort of Writer 
that stores to a stream or file)

```java
public exportToCsv(Stream<Person> employees) {
    try (StringWriter sw = new StringWriter();
         CSVWriter<CsvEntity> writer = new CSVWriter<>(sw, CsvEntity.class,
             CSVFormat.RFC4180.withDelimiter(';'), true))) {
      writer.write(employees.map(this::mapToCsvEntity));
      System.out.println(sw.toString());
    } catch (IOException ex) {
      logger.error("Could not write CSV file", ex);
    }
}
```
 The method `mapToCsvEntity` in this example would create an object that contains all the fields for export to CSV. Of course, you could skip the mapping, if you want to export the Employee object itself. If there are fields in that object which should not be exported, you could annotate those with `@CSVIgnore`.
 
 As you can see here, the export works stream-based, like in Apache Commons CSV. This means, you can
 save a lot of memory when storing large amounts of records.
 
 The `CSVFormat` class is from Apache Commons CSV. With this you can specify the format, like `DEFAULT`, `EXCEL`, or
 `RFC4180`. It is also possible to define a delimiter and other things. The only thing you should not use is one of the
`withHeader` methods, because that will be used internally to set the column headers found in the class of the objects
to export, in this case `CsvEntity`.

In the example class `CsvEntity` you see two unannotated fields. Those will be exported as `Firstname` and `Lastname`,
respectively. The third column will be exported as `Date of Birth`, which is configured with the annotation
`CSVColumnName`. You can also configure other behaviour like the `CSVDateFormat`.

Here is a list of Annotations


Annotation | Scope | Description
-----------|-------|------------
`@CSVColumnName` | field | sets a name that differs from the generated one
`@CSVDateFormat` | field | sets a date format of a `java.time.TemporalAccessor` like `LocalDate` or `LocalDateTime`
`@CSVDefaultColumnName` | class | selects a method to generate column names. See enum [`ColumnNameStyle`](src/main/java/me/landmesser/simplecsv/ColumnNameStyle.java) 
`@CSVIgnore` | field | will not export this field at all
`@CSVUseConverter` | class | at class level multiple converters can be given using the annotation `@CSVUseConverters`. Those will be used for the given type, unless a converter is given at a field. Converters must implement the interface [`CSVConverter`](src/main/java/me/landmesser/simplecsv/CSVConverter.java)
`@CSVUseConverter` | field | at field level the converter will only be used for the annotated field.
 
[1]: https://commons.apache.org/proper/commons-csv/
[2]: https://en.wikipedia.org/wiki/Comma-separated_values
[3]: https://mapstruct.org/ 
