package hadoop.mr.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
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
 * @create 2017/5/3
 * 10:00
 */
public class WordCountDriver
{
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException
    {
        Configuration conf = new Configuration();
        //conf.set("mapreduce.framework.name", "yarn");
        //conf.set("yarn.resourcemanager.hostname", "localhost");

        String inputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/wordcount/inputs";
        String outputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/wordcount/result";

        //conf.set("mapred.jar", "hadoop.jar");
        Job job = Job.getInstance(conf);

        //job.setJar("/home/hadoop/wc.jar");
        // 指定本程序的jar包所在的本地路径，需要提交给Yarn
        // setJar方法传入jar包绝对路径，必须将jar部署到指定位置，很不灵活
        // 而使用setJarByClass，可以从JVM中找到该类，比较常用
        job.setJarByClass(WordCountDriver.class);
        //job.setJar("hadoop.jar");

        // 指定本业务job要使用的Map和Reduce业务类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setCombinerClass(WordCountReducer.class);
        // 文件过多且过小，导致mapTask过多引起性能底下，可以将split合并
        job.setInputFormatClass(CombineTextInputFormat.class);
        // 通过设置合并后最大切片大小与最小切片大小来提升效率
        CombineTextInputFormat.setMaxInputSplitSize(job,4 * 1024);
        CombineTextInputFormat.setMinInputSplitSize(job,1 * 1024);
        //job.setNumReduceTasks(4);

        // 指定Mapper输数据的KV类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 指定最终输出数据的KV类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 指定job的输入源
        FileInputFormat.setInputPaths(job, new Path(inputPath));

        // 指定job的输出结构存储位置
        FileSystem fs = FileSystem.get(new URI("/"), conf, "root");
        if (fs.exists(new Path(outputPath)))
        {
            fs.delete(new Path(outputPath), true);
        }
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        // 将job中配置的相关参数，以及job所用的java类所在的jar包提交给yarn去运行
        //job.submit();
        // 以阻塞的方式提交job，便于查看job处理进度
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
