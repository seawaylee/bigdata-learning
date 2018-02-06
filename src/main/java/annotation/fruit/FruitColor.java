package annotation.fruit;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {


    Color color() default Color.RED;

    enum Color {
        GREEN, RED, YELLLOW
    }
}
