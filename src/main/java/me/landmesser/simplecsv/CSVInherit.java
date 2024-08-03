package me.landmesser.simplecsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If given, the fields from the base classes are exported in addition
 * to the fields of the class itself. The fields are being accessed via
 * getters and setters, which must be implemented to make this work.
 * <p>
 * You can set a strategy (fields of the base class first or last) which
 * is used for all classes in the hierarchy, unless the base class is itself
 * annotated with this annotation.
 * <p>
 * You can also define a depth which tells the parser how many base classes
 * in te hierarchy should be visited. Again, if in a base class this annotation
 * is also present with a different depth, it will be overridden. A depth of -1
 * means all predecessors until {@link Object} ist reached.
 * <p>
 * With <code>ignore</code> you can pass a comma-separated list of base class
 * members that should not be imported.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface CSVInherit {
  InheritanceStrategy value() default InheritanceStrategy.BASE_FIRST;

  int depth() default -1;

  String ignore() default "";
}
