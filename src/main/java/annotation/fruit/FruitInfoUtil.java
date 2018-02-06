package annotation.fruit;

import java.lang.reflect.Field;
import java.util.StringJoiner;

/**
 * @author NikoBelic
 * @create 2018/2/6 10:32
 */
public class FruitInfoUtil {
    public static void getFruitInfo(Class<?> clazz) {
        String strFruitName = "Fruit Name : ";
        String strFruitColor = "Fruit Color : ";
        String strFruitProvider = "Fruit Provider : ";

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FruitName.class)) {
                FruitName fruitName = field.getAnnotation(FruitName.class);
                strFruitName += fruitName.value();
                System.out.println(strFruitName);
            } else if (field.isAnnotationPresent(FruitColor.class)) {
                FruitColor fruitColor = field.getAnnotation(FruitColor.class);
                strFruitColor += fruitColor.color().toString();
                System.out.println(strFruitColor);
            } else if (field.isAnnotationPresent(FruitProvider.class)) {
                FruitProvider fruitProvider = field.getAnnotation(FruitProvider.class);
                strFruitProvider += new StringJoiner(",").add(String.valueOf(fruitProvider.id())).add(fruitProvider.name()).add(fruitProvider.address()).toString();
                System.out.println(strFruitProvider);
            }
        }

    }
}
