package basic_test;

/**
 * @author NikoBelic
 * @create 2017/12/22 10:11
 */
public class PrivateFieldsExtendsTest
{
    public static void main(String[] args)
    {
        Human human = new Student();
        human.setName("Niko");
        human.setGender("Male");
        System.out.println(human.getName());
    }
}

class Student extends Human
{
    private String classNum;

    public String getClassNum()
    {
        return classNum;
    }

    public void setClassNum(String classNum)
    {
        this.classNum = classNum;
    }
}

class Human
{
    private String name;
    private String gender;


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }
}
