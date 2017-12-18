package pre_course.reflection;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Java反射测试
 * @author NikoBelic
 * @create 2017/4/10 14:19
 */
public class ReflectionTest<T>
{
    /**
     * 通过class类路径创建对象
     *
     * @param className 类的相对路径
     * @param params    构造器的参数
     * @param _private  是否使用私有构造函数
     * @Author SeawayLee
     * @Date 2017/04/10 14:27
     */
    public T getInstance(String className, Map<Object, Object> params, Boolean _private) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException, IOException
    {
        Class clazz = Class.forName(className);
        Class<?>[] paramTypes = new Class<?>[params != null ? params.size() : 0];
        Object[] paramValues = new Object[params != null ? params.size() : 0];
        Constructor constructor;
        T instance;

        if (params == null)
        {
            instance = (T) clazz.newInstance();
            Field name = clazz.getDeclaredField("name");
            Field age = clazz.getDeclaredField("age");
            name.setAccessible(true);
            age.setAccessible(true);
            name.set(instance, "默认name");
            age.set(instance, 99);
        } else
        {
            int index = 0;
            for (Map.Entry entry : params.entrySet())
            {
                paramTypes[index] = (Class<?>) entry.getKey();
                paramValues[index] = entry.getValue();
                index++;
            }
            constructor = !_private ? clazz.getConstructor(paramTypes) : clazz.getDeclaredConstructor(paramTypes);

            constructor.setAccessible(true); // 强制取消Java的权限检测
            instance = (T) constructor.newInstance(paramValues);
        }
        // 获取私有方法
        Method method = clazz.getDeclaredMethod("talk");
        method.setAccessible(true);
        Object invokeResult = method.invoke(instance);
        System.out.println("私有函数反射结果输出:" + invokeResult);
        // 获取类加载器
        System.out.println("类加载器：" + clazz.getClassLoader());
        // 获取接口
        System.out.println("接口：");
        for (Class c : clazz.getInterfaces())
             System.out.println(c);
        // 获取父类
        System.out.println("父类：" + clazz.getGenericSuperclass());
        // 获取静态资源文件
        byte[] buff = new byte[1024];
        clazz.getResourceAsStream("config.properties").read(buff);
        System.out.println("配置文件:" + new String(buff,"UTF-8"));

        return instance;
    }


    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException, IOException
    {
        ReflectionTest<Person> ref = new ReflectionTest();
        // 构造参数
        Map<Object, Object> params = new HashMap<>();
        params.put(String.class, "NikoBelic");
        //params.put(Integer.class, 18);

        //Person p = ref.getInstance("Person", params, true); // 通过有参构造器创建实例
        Person p = ref.getInstance("Person", null, true); // 通过午餐构造器创建实例
        System.out.println(p);
    }
}
