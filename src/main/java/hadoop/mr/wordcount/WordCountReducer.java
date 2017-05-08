package hadoop.mr.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.net.Inet4Address;

/**
 * KEYIN,VALUEIN 对应 Mapper输出的KEYOUT,VALUEOUT
 * KEYOUT,VALUEOUT 是自动以Reduce逻辑处理结果的输出数据类型
 * KEYOUT是单词，VALUEOUT是总次数
 * @author NikoBelic
 * @create 2017/5/3 09:49
 */
public class WordCountReducer extends Reducer<Text,IntWritable,Text,IntWritable>
{
    /**
     * ReduceTask处理逻辑
     * <Niko,1> <Niko,1> <Niko,1> <Niko,1>
     * <Belic,1> <Belic,1> <Belic,1> <Belic,1>
     * @param key 一组相同单词kv的key
     * @param values
     *
     * @Author SeawayLee
     * @Date 2017/05/03 09:52
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
    {
        int count = 0;
        for (IntWritable value : values)
        {
            count += value.get();
        }

        context.write(key, new IntWritable(count));
        System.out.println("ReduceTask正在处理第 " + key.toString() + " 行文本,ObjHashCode:" + this.hashCode() + ",IP:"  + Inet4Address.getLocalHost().getHostAddress());
    }
}
