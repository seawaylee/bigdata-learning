package java8;

import org.mockito.internal.matchers.Null;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author NikoBelic
 * @create 2017/10/16 13:37
 */
public class StreamApi
{
    static class Person
    {
        private String name;
        private int age;
        private int no;

        public Person(String name, int age, int no)
        {
            this.name = name;
            this.age = age;
            this.no = no;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getAge()
        {
            return age;
        }

        public void setAge(int age)
        {
            this.age = age;
        }

        public int getNo()
        {
            return no;
        }

        public void setNo(int no)
        {
            this.no = no;
        }


        @Override
        public String toString()
        {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", no=" + no +
                    '}';
        }
    }

    public void baseTest() throws IOException
    {
        Person p2 = new Person("Belic", 16, 2);
        Person p5 = new Person("Ben", 12, 5);
        Person p1 = new Person("Niko", 20, 1);
        Person p4 = new Person("Helen", 43, 4);
        Person p3 = new Person("Tom", 42, 3);

        List<Person> personList = Arrays.asList(p1, p2, p3, p4, p5);
        // Stream操作集合
        String name = personList.stream().sorted(Comparator.comparingInt(x -> x.getNo())).findFirst().get().getName();
        System.out.println("序号最小的是" + name);
        // 内部迭代
        long count = personList.stream().filter(p -> p.getAge() > 20).count();
        // List得到Stream
        String content = Files.readAllLines(Paths.get("/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/data/paper.txt")).stream().collect(Collectors.joining("|"));
        System.out.println(content);
        // map  A -> B;   filter  aList -> aSubList
        List<String> list = personList.stream().map(Person::getName).filter(_name -> _name.equals("Niko")).collect(Collectors.toList());
        for (String s : list)
        {
            System.out.println(s);
        }
        // flatMap  aList + bList -> abList
        List<List<Person>> classList = new ArrayList<>();
        classList.add(Arrays.asList(p1, p2));
        classList.add(Arrays.asList(p3, p4));
        classList.add(Arrays.asList(p5));
        classList.stream().flatMap(Collection::stream).map(person -> {
            System.out.println(person.getName());
            return null;
        }).collect(Collectors.toSet());
        // 并行stream
        classList.parallelStream().flatMap(Collection::stream).map(person -> {
            System.out.println(person.getName());
            return null;
        }).collect(Collectors.toSet());
    }

    public static void main(String[] args) throws IOException
    {
        List<Integer> source = new ArrayList<>();
        source.add(126);
        source.add(127);
        source.add(128);
        source.add(8888888);
        List<Integer> test = new ArrayList<>();
        test.add(4);
        test.add(5);
        test.add(6);
        test.add(7);
        // 123
        //List<Integer> collect = source.stream().filter(sourceValue -> test.stream().allMatch(testValue -> !testValue.equals(sourceValue))).collect(Collectors.toList());
        // 4
        //List<Integer> collect = source.stream().filter(sourceValue -> test.stream().anyMatch(testValue -> testValue.equals(sourceValue))).collect(Collectors.toList());
        //System.out.println(collect);
        System.out.println(source.contains(126));
        System.out.println(source.contains(127));
        System.out.println(source.contains(128));
        System.out.println(source.contains(8888888));
    }
}
