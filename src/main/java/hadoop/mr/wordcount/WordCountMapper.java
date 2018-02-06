package hadoop.mr.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.net.Inet4Address;

/**
 * KEYIN: 默认情况下，是MR框架锁读到的一行文本的起始偏移量，Long
 * VALUEIN: 默认情况下，是MR框架所读到一行文本的内容，String
 * KEYOUT: 是用户自定义逻辑处理完成后输出数据中的Key，在此处是单词，String
 * VALUEOUT: 是用户自定义逻辑处理完成后输出数据中的Value，在此处是单词次数，Integer
 *
 * 在Hadoop中有更精简的序列化接口（纯粹的数据），所以不直接用Java中的基本数据类型对象（虽然支持序列化，但是有很多冗余信息例如继承结构），而用LongWritable
 * @author NikoBelic
 * @create 2017/5/3 09:16
 */
public class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable>
{
    /**
     * Map阶段的业务逻辑
     * MapTask会对每一行输入数据调用一次此方法
     * @Author SeawayLee
     * @Date 2017/05/03 09:29
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        System.out.println(((FileSplit) context.getInputSplit()).getPath().getName());
        // 将文本内容转换成字符串
        String line = value.toString();
        // 对一行文本切分成单词数组
        String[] words = line.split(" ");
        // 将单词输出为<word,1>
        for (String word : words)
        {
            // 将单词作为key，将次数1作为value，以便于后续的数据分发
            // 可以根据单词分发，以便于相同的单词会到相同的ReduceTask中
            context.write(new Text(word), new IntWritable(1));
        }
        //System.out.println("MapTask正在处理第 " + key.toString() + " 行文本,ObjHashCode:" + this.hashCode() + ",IP:"  + Inet4Address.getLocalHost().getHostAddress());
    }
}
