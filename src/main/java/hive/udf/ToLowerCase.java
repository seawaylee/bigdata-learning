package hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @author NikoBelic
 * @create 2017/9/11 17:41
 */
public class ToLowerCase extends UDF
{
    public String evaluate(String field)
    {
        return field.toUpperCase();
    }
}
