package L23_Reflection_Annotation.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    int order() default Integer.MAX_VALUE;
    String description() default "";
    boolean enabled() default true;
}
