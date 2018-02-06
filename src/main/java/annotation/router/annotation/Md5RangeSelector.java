package annotation.router.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Md5RangeSelector{

    char start();

    char end();

}
