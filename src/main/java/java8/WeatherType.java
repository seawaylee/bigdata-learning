package java8;

/**
 * @author NikoBelic
 * @create 2017/12/19 10:38
 */
public enum WeatherType
{
    SPRING("spring"),
    SUMMER("summer"),
    FALL("fall"),
    WINTER("winter");

    private String weatherTypeStr = null;

    WeatherType()
    {
    }

    WeatherType(String weatherTypeStr)
    {
        this.weatherTypeStr = weatherTypeStr;
    }

    public String getWeatherTypeStr()
    {
        return weatherTypeStr;
    }

    public void setWeatherTypeStr(String weatherTypeStr)
    {
        this.weatherTypeStr = weatherTypeStr;
    }
}
