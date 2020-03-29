package me.landmesser.simplecsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface CSVInherit {
  InheritanceStrategy value() default InheritanceStrategy.BASE_FIRST;

  int depth() default -1;
}
