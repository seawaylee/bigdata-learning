package hive.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * @author NikoBelic
 * @create 2017/12/20 11:51
 */
public class ActivityUserUDF extends UDF
{
    private static Map<String, String> map = new HashMap<>();


    public Map<String, String> evaluate(final Text s)
    {
        JSONObject commonJson = JSON.parseObject(s.toString());
        commonJson.entrySet().forEach(entry -> map.put(entry.getKey(), entry.getValue().toString()));
        map.put("remark", "卧槽牛逼啊老铁");
        return map;
    }
}
