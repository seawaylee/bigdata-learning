package pre_course.reflection;

import java.io.Serializable;

/**
 * @author NikoBelic
 * @create 2017/4/10 14:20
 */
public class Person implements Serializable
{
    private String name;
    private Integer age;

    public Person() {
    }

    private Person(String name)
    {
        this.name = name;
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    private String talk()
    {
        return "I said that .....";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
