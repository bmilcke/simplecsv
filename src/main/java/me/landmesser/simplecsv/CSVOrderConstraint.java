package me.landmesser.simplecsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(CSVOrderConstraints.class)
public @interface CSVOrderConstraint {
  String value();
  String before() default "";
  String after() default "";
}
