package me.landmesser.simplecsv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * At class level you can define a converter being used
 * for all objects of the type you set with <code>forType</code>.
 * This field is required at class level.
 * <p>
 * At field level, you can define a converter to be used
 * only for the annotated field. In this case the value
 * of <code>forType</code> is ignored.
 * </p>
 * <p>
 * The converter you pass must be of type {@link CSVConverter}&lt;T&gt;
 * where <code>T</code> must match the type of the field being annotated.
 * </p>
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CSVUseConverters.class)
public @interface CSVUseConverter {
  @SuppressWarnings("rawtypes")
  Class<? extends CSVConverter> value();

  Class<?> forType() default Void.class;
}
