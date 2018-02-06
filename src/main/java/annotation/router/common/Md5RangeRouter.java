package annotation.router.common;

import annotation.router.annotation.Md5RangeSelector;
import com.google.common.collect.Maps;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author NikoBelic
 * @create 2018/2/6 15:17
 */
public class Md5RangeRouter<I> implements Router<I, String> {

    private final Map<IntegerRange, I> GROUP_MAPPER = Maps.newHashMap();
    private final int md5Index = 31;

    @Override
    public I get(String s) {
        char md5 = Md5Utils.md5Hex(s).charAt(md5Index);
        System.out.println(s + " -> " + md5);
        for (Map.Entry<IntegerRange, I> rangeEntry : GROUP_MAPPER.entrySet()) {
            if (rangeEntry.getKey().between(md5)) {
                return rangeEntry.getValue();
            }
        }
        return null;
    }

    public Md5RangeRouter() {
        System.out.println("Md5RangeRouter Constructor");
    }

    @PostConstruct
    public void init() throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Md5RangeSelector[] selectors = field.getAnnotationsByType(Md5RangeSelector.class);
            if (selectors == null) {
                continue;
            }
            field.setAccessible(true);
            I service = (I) field.get(this);

            for (Md5RangeSelector selector : selectors) {

                IntegerRange range = new IntegerRange(selector.start(), selector.end());
                GROUP_MAPPER.put(range, service);
            }
        }
    }
}
