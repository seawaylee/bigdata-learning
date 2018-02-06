package annotation.fruit;

/**
 * @author NikoBelic
 * @create 2018/2/6 10:39
 */
public class Apple {

    @FruitName(value = "Apple")
    String name;

    @FruitColor(color = FruitColor.Color.GREEN)
    String color;

    @FruitProvider(id = 1, name = "Apple Provider", address = "BeiJing")
    String provider;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
