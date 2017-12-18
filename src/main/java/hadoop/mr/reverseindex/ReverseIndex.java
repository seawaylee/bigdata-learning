package hadoop.mr.reverseindex;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

/**
 * @author NikoBelic
 * @create 2017/5/24 15:07
 */
public class ReverseIndex
{
    public static void main(String[] args) throws Exception
    {
        String inputPath1 = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/revers_index";
        String outputPath1 = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/revers_index/result1";
        String inputPath2 = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/revers_index/result1/part-r-00000";
        String outputPath2 = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/revers_index/result2";
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        //conf.set("mapreduce.framework.name", "yarn");
        //conf.set("yarn.resourcemanager.hostname", "hadoop1");

        Job job1 = Job.getInstance(conf,"Job1");


        job1.setMapperClass(ReverseIndexMapperStepOne.class);
        job1.setReducerClass(ReverseIndexReducerStepOne.class);
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(LongWritable.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(job1, new Path(inputPath1));
        FileSystem fs = FileSystem.get(new URI(outputPath1), conf, "NikoBelic");
        if (fs.exists(new Path(outputPath1)))
        {
            fs.delete(new Path(outputPath1));
        }
        FileOutputFormat.setOutputPath(job1, new Path(outputPath1));

        //job1.waitForCompletion(true);


        Job job2 = Job.getInstance(conf,"Job2");
        job2.setMapperClass(ReverseIndexMapperStepTwo.class);
        job2.setReducerClass(ReverseIndexReducerStepTwo.class);

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(Text.class);

        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);


        FileInputFormat.setInputPaths(job2, new Path(inputPath2));
        if (fs.exists(new Path(outputPath2)))
        {
            fs.delete(new Path(outputPath2));
        }
        FileOutputFormat.setOutputPath(job2, new Path(outputPath2));

        job1.waitForCompletion(true);

        ControlledJob ctrlJob1 = new ControlledJob(conf);
        ctrlJob1.setJob(job1);

        ControlledJob ctrlJob2 = new ControlledJob(conf);
        ctrlJob2.setJob(job2);

        JobControl jobControl = new JobControl("MyControl");
        jobControl.addJob(ctrlJob1);
        jobControl.addJob(ctrlJob2);
        jobControl.run();
        System.out.println(jobControl.getFailedJobList());
        jobControl.stop();
    }
    static class ReverseIndexMapperStepOne extends Mapper<LongWritable, Text, Text, LongWritable>
    {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
        {
            FileSplit fileSplit = (FileSplit) context.getInputSplit();
            String[] fields = value.toString().split(" ");
            String fileName = fileSplit.getPath().getName();
            for (String field : fields)
            {
                context.write(new Text(field + "--" + fileName), new LongWritable(1L));
            }

        }
    }

    static class ReverseIndexReducerStepOne extends Reducer<Text, LongWritable, Text, LongWritable>
    {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException
        {
            Long totalCount = 0L;
            for (LongWritable value : values)
            {
                totalCount += value.get();
            }
            context.write(key, new LongWritable(totalCount));
        }
    }

    static class ReverseIndexMapperStepTwo extends Mapper<LongWritable, Text, Text, Text>
    {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
        {
            String[] fields = value.toString().split("--");
            String word = fields[0];
            String freq = fields[1];

            context.write(new Text(word),new Text(freq));

        }
    }

    static class ReverseIndexReducerStepTwo extends Reducer<Text, Text, Text, Text>
    {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
        {
            StringBuilder sb = new StringBuilder();
            for (Text value : values)
            {
                sb.append(value).append("   ");
            }
            context.write(key, new Text(sb.toString()));
        }
    }

}
