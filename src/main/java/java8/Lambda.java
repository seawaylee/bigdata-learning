package java8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author NikoBelic
 * @create 2017/10/16 13:06
 */
public class Lambda
{
    public static void doFun1(int x, int y, MyInterface myInterface)
    {
        myInterface.fun1(x, y);
    }

    public static void main(String[] args)
    {
        // Hello Lambda
        new Thread(() -> System.out.println("Hello Lambda")).start();

        // expression = (variable) -> action

        doFun1(1, 2, new MyInterface()
        {
            @Override
            public void fun1(int x, int y)
            {
                System.out.println(x + y);
            }
        });

        doFun1(1, 2, (x, y) -> System.out.println(x + y));

        // Default methond
        List<Integer> list = Arrays.asList(2, 7, 3, 1, 8, 6, 4);
        //list.sort((x,y) -> {return x - y;});
        list.sort(Comparator.naturalOrder());
        System.out.println(list);
    }
}

@FunctionalInterface
interface MyInterface
{
    void fun1(int x, int y);
}