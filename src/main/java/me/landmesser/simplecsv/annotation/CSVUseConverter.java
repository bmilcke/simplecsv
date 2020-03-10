package me.landmesser.simplecsv.annotation;

import me.landmesser.simplecsv.converter.CSVConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CSVUseConverters.class)
public @interface CSVUseConverter {
  @SuppressWarnings("rawtypes")
  Class<? extends CSVConverter> value();

  Class<?> forType() default Void.class;
}
