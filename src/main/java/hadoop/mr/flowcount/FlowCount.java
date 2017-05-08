package hadoop.mr.flowcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

/**
 * @author NikoBelic
 * @create 2017/5/7 22:54
 */
public class FlowCount
{
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, ClassNotFoundException
    {
        String inputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/flow.txt";
        String outputPath = "hdfs://localhost:9000/flowbean/result";
        Configuration conf = new Configuration();
        //conf.set("mapreduce.framework.name", "yarn");
        //conf.set("yarn.resourcemanager.hostname", "localhost");

        Job job = Job.getInstance(conf);
        job.setJarByClass(FlowCount.class);

        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job, new Path(inputPath));

        FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf, "NikoBelic");
        if (fs.exists(new Path(outputPath)))
        {
            fs.delete(new Path(outputPath));
        }
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        job.waitForCompletion(true);
    }
}
class FlowCountMapper extends Mapper<LongWritable,Text,Text,FlowBean>
{

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String[] values = value.toString().split("\\t");
        String phoneNumber = values[1];
        Long upFlow = Long.parseLong(values[values.length - 3]);
        Long downFlow = Long.parseLong(values[values.length - 2]);
        FlowBean flowBean = new FlowBean(upFlow,downFlow);
        context.write(new Text(phoneNumber),flowBean);
    }
}

class FlowCountReducer extends Reducer<Text,FlowBean,Text,FlowBean>
{
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException
    {
        Iterator<FlowBean> iterator = values.iterator();
        FlowBean currBean;
        Long totalUp = 0L;
        Long totalDown = 0L;
        while (iterator.hasNext())
        {
            currBean = iterator.next();
            totalUp += currBean.getUpFlow();
            totalDown += currBean.getDownFlow();
        }
        context.write(key, new FlowBean(totalUp,totalDown));
        //Thread.sleep(1000);
    }
}