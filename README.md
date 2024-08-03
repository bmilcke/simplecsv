# simplecsv: _Java Library for exporting Objects to CSV_

[![Latest release](https://img.shields.io/github/release/bmilcke/simplecsv.svg)](https://github.com/bmilcke/simplecsv/releases/latest)
![Java CI](https://github.com/bmilcke/simplecsv/workflows/Java%20CI/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/me.landmesser.simplecsv/simplecsv.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22me.landmesser.simplecsv%22%20AND%20a:%22simplecsv%22)

The goal of this library is to enable you to create and parse CSV-files (or streams) easily. CSV is a simple
data format which puts values into a line of text separated by a delimiter, which is typically a comma or
semicolon. Thus, the name CSV for **C**omma **S**eparated **V**alues. (see [Wikipedia][2])

This library bases upon [Commons CSV from Apache Commons][1]. In addition to the functionality of commons CSV, with
simplecsv you can create DTO-like objects that describe the fields you want to export to CSV. 

The [CSVWriter](src/main/java/me/landmesser/simplecsv/CSVWriter.java) and the [CSVReader](src/main/java/me/landmesser/simplecsv/CSVReader.java) 
both work stream-based. This allows you to process large amounts of data without wasting a lot of memory.

## Adding simplecsv to your build

You can add this library to your Maven-based project by adding this dependency to the `dependencies`-section 
of your `pom.xml`.

```xml
<dependency>
  <groupId>me.landmesser</groupId>
  <artifactId>simplecsv</artifactId>
  <version>1.0</version>
</dependency>
```

## Usage

Especially when dealing with CSV files with a lot of columns, you may lose track. First, you send a bunch of strings 
as headers to the writer and then the values are written by calling lots of getters of an object, maybe using conversion 
routines to format. To make this easier to read and more cohesive, you can write a class containing all the
fields you want to export in the order you want them to be exported. Take a look at this example. 

```java
public class Person {
  
  private String firstname;

  private String lastname;

  @CSVIgnore
  private String nickname;

  @CSVColumnName("Date of Birth")
  @CSVDateFormat("dd.MM.yyyy")
  private LocalDate dateOfBirth;

  // getters and setters
}
```

You can just export objects of this type by using the following code. (Here a StringWriter is used for simplicity, but of course you will typically use some sort of Writer 
that stores to a stream or file)

```java
public exportToCsv(Stream<Person> employees) {
    try (StringWriter sw = new StringWriter();
         CSVWriter<Person> writer = new CSVWriter<>(sw, Person.class,
             CSVFormat.RFC4180.withDelimiter(';'), /* withHeaders */ true)) {
      writer.write(employees);
      System.out.println(sw);
    } catch (IOException ex) {
      logger.error("Could not write CSV file", ex);
    }
}
```
As you can see here, the export works stream-based, like in Apache Commons CSV. This means, you can save 
memory when storing large amounts of records.
 
 The `CSVFormat` class is from Apache Commons CSV. With this you can specify the format, like `DEFAULT`, `EXCEL`, or
 `RFC4180`. It is also possible to define a delimiter and other things. The only thing you should not use is one of the
`withHeader` methods, because that will be used internally to set the column headers found in the class of the objects
to export, in this case `Person`.

So, what will be exported to CSV for this little example? You might see something like this:

```
Firstname;Lastname;Date of Birth
Jack;Smith;25.04.1980
"Hans Werner";Hofstaedter;01.02.1974
```

The headers are generated out of the field names using the given strategy, which is capitalization of the fieldname by default. You
can control this behaviour by adding an annotation of type [CSVDefaultColumnName](src/main/java/me/landmesser/simplecsv/CSVDefaultColumnName.java)
at class level.

The field `nickname` will not be exported, because it is annotated with [CSVIgnore](src/main/java/me/landmesser/simplecsv/CSVIgnore.java).

Finally, the date of birth will get a custom header, as the annotation [CSVColumnName](src/main/java/me/landmesser/simplecsv/CSVColumnName.java)
sets it. In addition, the date format is also specified, so it is not exported as the default ISO-format.

Here is a list of all annotations available


| Annotation                | Scope | Description                                                                                                                                                                                                                                                                                            |
|---------------------------|-------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `@CSVColumnName`          | field | sets a name that differs from the generated one                                                                                                                                                                                                                                                        |
| `@CSVDateFormat`          | field | sets a date format of a `java.time.TemporalAccessor` like `LocalDate` or `LocalDateTime` or an old-school `java.util.Date`                                                                                                                                                                             |
| `@CSVDefaultColumnName`   | class | selects a method to generate column names. See enum [`ColumnNameStyle`](src/main/java/me/landmesser/simplecsv/ColumnNameStyle.java)                                                                                                                                                                    |
| `CSVExportImportStrategy` | class | determines if all fields, or only those with a valid getter or getter and setter are to be exported. See enum [`ExportImportStrategy`](src/main/java/me/landmesser/simplecsv/ExportImportStrategy.java).                                                                                               |
| `@CSVIgnore`              | field | will not export this field at all                                                                                                                                                                                                                                                                      |
| `@CSVInherit`             | class | determines, if members of base classes are included. See enum [`InheritanceStrategy`](src/main/java/me/landmesser/simplecsv/InheritanceStrategy.java) for strategies according the order of base members. You can also set a `depth` that determines how far the inheritance chain should be followed. |
| `@CSVOrderConstraint`     | class | defines a constraint for one field being before or after another one. You pass the field to be moved and a field before or after which it should be moved. You can pass multiple constraints with the annotation `@CSVOrderConstraints`.                                                               |
| `@CSVOrderFields`         | class | defines an ordering for the fields. You can pass a class of type [`FieldOrder`](src/main/java/me/landmesser/simplecsv/FieldOrder.java) for defining the ordering. Default is sorting ascending (see [`SortFieldOrder`](src/main/java/me/landmesser/simplecsv/SortFieldOrder.java))                     |
| `@CSVUseConverter`        | class | at class level multiple converters can be given using the annotation `@CSVUseConverters`. Those will be used for the given type, unless a converter is given at a field. Converters must implement the interface [`CSVConverter`](src/main/java/me/landmesser/simplecsv/CSVConverter.java)             |
| `@CSVUseConverter`        | field | at field level the converter will only be used for the annotated field.                                                                                                                                                                                                                                |

[1]: https://commons.apache.org/proper/commons-csv/
[2]: https://en.wikipedia.org/wiki/Comma-separated_values
