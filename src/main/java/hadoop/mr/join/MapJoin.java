package hadoop.mr.join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NikoBelic
 * @create 2017/5/24 09:55
 */
public class MapJoin
{
    public static void main(String[] args) throws Exception
    {
        String inputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/join/order.txt";
        String outputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/join/result";
        Configuration conf = new Configuration();
        //conf.set("mapreduce.framework.name", "yarn");
        //conf.set("yarn.resourcemanager.hostname", "hadoop1");

        Job job = Job.getInstance(conf);
        //job.setUser("NikoBelic");
        //job.setJarByClass(FlowCount.class);
        job.addCacheFile(new URI("/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/join/product.txt"));
        job.setUser("NikoBelicll");
        job.setMapperClass(MapperJoin.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

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

class MapperJoin extends Mapper<LongWritable, Text, Text, NullWritable>
{
    Map<String, String> productMap = new HashMap<>();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException
    {
        URI[] cacheFiles = context.getCacheFiles();
        for (URI cacheFile : cacheFiles)
        {
            System.out.println(cacheFile.getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile.getPath())));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                String[] fields = line.split(",");
                productMap.put(fields[0], fields[1]);
            }
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String[] fields = value.toString().split(",");
        context.write(new Text(value.toString() + "," + productMap.get(fields[2])), NullWritable.get());
    }
}