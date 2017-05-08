package mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Driver相当于Yarn集群的客户端
 * 需要在此封装MR程序的相关运行参数，指定jar包
 * 最后提交给Yarn
 *
 * @author NikoBelic
 * @create 2017/5/3 10:00
 */
public class WordCountDriver
{
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException
    {
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name", "yarn");
        conf.set("yarn.resourcemanager.hostname", "10.5.151.241");
        Job job = Job.getInstance(conf);
        job.setUser("root");

        //job.setJar("/home/hadoop/wc.jar");
        // 指定本程序的jar包所在的本地路径，需要提交给Yarn
        // setJar方法传入jar包绝对路径，必须将jar部署到指定位置，很不灵活
        // 而使用setJarByClass，可以从JVM中找到该类，比较常用
        job.setJarByClass(WordCountDriver.class);
        job.setJar("/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/out/artifacts/hadoop_jar/hadoop.jar");

        // 指定本业务job要使用的Map和Reduce业务类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 指定Mapper输数据的KV类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 指定最终输出数据的KV类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 指定job的输入源

        FileInputFormat.setInputPaths(job, new Path(args[0]));

        // 指定job的输出结构存储位置
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop1:9000"), conf, "root");
        if (fs.exists(new Path(args[1])))
        {
            fs.delete(new Path(args[1]), true);
        }
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 将job中配置的相关参数，以及job所用的java类所在的jar包提交给yarn去运行
        //job.submit();
        // 以阻塞的方式提交job，便于查看job处理进度
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}