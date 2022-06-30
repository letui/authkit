package cc.jstr.authkit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthzResource {
    String name();
    String displayName() default "";
    String category() default "";
    String iconUri() default "";

}
