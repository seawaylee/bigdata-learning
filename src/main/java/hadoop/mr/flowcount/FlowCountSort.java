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

/**
 * @author NikoBelic
 * @create 2017/5/15 21:37
 */
public class FlowCountSort
{
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, ClassNotFoundException
    {
        String inputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/flow/result/part-r-00000";
        String outputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/flowsort/result";
        Configuration conf = new Configuration();
        //conf.set("mapreduce.framework.name", "yarn");
        //conf.set("yarn.resourcemanager.hostname", "localhost");

        Job job = Job.getInstance(conf);
        job.setJarByClass(FlowCountSort.class);

        job.setMapperClass(FlowCountSortMapper.class);
        job.setReducerClass(FlowCountSortReducer.class);

        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //job.setPartitionerClass(FlowCountPartitioner.class);
        //job.setNumReduceTasks(4);

        FileInputFormat.setInputPaths(job, new Path(inputPath));

        FileSystem fs = FileSystem.get(new URI(outputPath), conf, "NikoBelic");
        if (fs.exists(new Path(outputPath)))
        {
            fs.delete(new Path(outputPath));
        }
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        job.waitForCompletion(true);
    }
}


class FlowCountSortMapper extends Mapper<LongWritable, Text, FlowBean, Text>
{
    private FlowBean resultBean = new FlowBean();
    private Text v = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String values[] = value.toString().split(" ");
        Long upFlow = Long.valueOf(values[1]);
        Long downFlow = Long.valueOf(values[2]);
        Long sumFlow = Long.valueOf(values[values.length - 1]);
        String phoneNumber = values[0];
        v.set(phoneNumber);
        resultBean.set(upFlow, downFlow, sumFlow);
        context.write(resultBean, v);
    }
}

class FlowCountSortReducer extends Reducer<FlowBean, Text, Text, FlowBean>
{
    @Override
    protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException
    {
        context.write(values.iterator().next(), key);
    }
}