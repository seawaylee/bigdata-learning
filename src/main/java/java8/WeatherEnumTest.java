package java8;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NikoBelic
 * @create 2017/12/19 10:36
 */

public class WeatherEnumTest
{
    public static void main(String[] args)
    {
        //WeatherType Spring = WeatherType.SPRING;
        //System.out.println(Spring.getWeatherTypeStr());
        IntEnum yi = IntEnum.SAN;
        System.out.println(yi.ordinal());
        List<IntEnum> intList = new ArrayList<>();
        intList.add(IntEnum.SAN);
        intList.add(IntEnum.ER);
        intList.add(IntEnum.YI);
        System.out.println(JSON.toJSONString(intList));

    }

    enum IntEnum
    {
        YI,
        ER,
        SAN;
    }
}


