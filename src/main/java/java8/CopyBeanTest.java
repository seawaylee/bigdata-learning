package java8;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author NikoBelic
 * @create 2017/12/12 09:24
 */
public class CopyBeanTest
{
    public static void main(String[] args)
    {
        CommonBean commonBean = new CommonBean();
        BeanA beanA = new BeanA();
        BeanB beanB = new BeanB();
        beanA.setCommonA(1);
        beanB.setCommonB(2);
        System.out.println("Before:" + commonBean);
        commonBean.doMerge(beanA, beanB);
        System.out.println("After:" + commonBean);
    }
}

class CommonBean
{
    protected int commonA;
    protected int commonB;

    public int getCommonA()
    {
        return commonA;
    }

    public void setCommonA(int commonA)
    {
        this.commonA = commonA;
    }

    public int getCommonB()
    {
        return commonB;
    }

    public void setCommonB(int commonB)
    {
        this.commonB = commonB;
    }

    public void doMerge(BeanA beanA, BeanB beanB)
    {
        List<Field> commonFields = Arrays.asList(this.getClass().getDeclaredFields());

        commonFields.forEach(commonField -> {
            try
            {
                Object beanAFieldValue = commonField.get(beanA);
                Object beanBFieldValue = commonField.get(beanB);
                if (beanAFieldValue != null
                        && Float.valueOf(beanAFieldValue.toString()) != 0)
                {
                    commonField.set(this, beanAFieldValue);
                } else if (beanBFieldValue != null
                        && Float.valueOf(beanBFieldValue.toString()) != 0)
                {
                    commonField.set(this, beanBFieldValue);
                }
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String toString()
    {
        return "commonBean{" +
                "commonA=" + commonA +
                ", commonB=" + commonB +
                '}';
    }
}

class BeanA extends CommonBean
{
    private int fieldA;

    public int getFieldA()
    {
        return fieldA;
    }

    public void setFieldA(int fieldA)
    {
        this.fieldA = fieldA;
    }

    @Override
    public String toString()
    {
        return "BeanA{" +
                "fieldA=" + fieldA +
                '}';
    }
}

class BeanB extends CommonBean
{
    private int fieldB;

    public int getFieldB()
    {
        return fieldB;
    }

    public void setFieldB(int fieldB)
    {
        this.fieldB = fieldB;
    }

    @Override
    public String toString()
    {
        return "BeanB{" +
                "fieldB=" + fieldB +
                '}';
    }
}
