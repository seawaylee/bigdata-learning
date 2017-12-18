package test;

import java.util.ArrayList;

/**
 * @author NikoBelic
 * @create 2017/5/23 15:34
 */
public class NullTest<T>
{

    public static void main(String[] args)
    {
        NullTest t = new NullTest();
        ArrayList list = t.buildArrayList("asd", null, null, 123);
        for (Object o : list)
        {
            System.out.println(o);
        }
        t.print(new Object[]{123, null, null, 567});

    }

    public ArrayList<T> buildArrayList(T... objs)
    {
        ArrayList<T> list = new ArrayList<>();
        for (T obj : objs)
        {
            list.add(obj);
        }
        return list;
    }

    public void print(Object[] objs)
    {
        for (Object obj : objs)
        {
            System.out.println(obj);
        }
    }
}
