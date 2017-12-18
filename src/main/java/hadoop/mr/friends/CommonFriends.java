package hadoop.mr.friends;

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
import java.util.Arrays;

/**
 * @author NikoBelic
 * @create 2017/5/23 12:58
 */
public class CommonFriends
{
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, ClassNotFoundException
    {
        String inputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/friends/result";
        String outputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/friends/result2";
        Configuration conf = new Configuration();
        //conf.set("mapreduce.framework.name", "yarn");
        //conf.set("yarn.resourcemanager.hostname", "hadoop1");

        Job job = Job.getInstance(conf);
        //job.setUser("NikoBelic");
        //job.setJarByClass(FlowCount.class);

        job.setMapperClass(FriendsMapperStepTwo.class);
        job.setReducerClass(FriendsReducerStepOne.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

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


class FriendsMapperStepOne extends Mapper<LongWritable, Text, Text, Text>
{
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String host = value.toString().split(":")[0];
        String[] friends = value.toString().split(":")[1].split(",");
        for (String friend : friends)
        {
            context.write(new Text(friend), new Text(host));
        }
    }
}


class FriendsReducerStepOne extends Reducer<Text, Text, Text, Text>
{
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
    {
        StringBuilder sb = new StringBuilder();
        for (Text value : values)
        {
            sb.append(value).append(",");
        }
        context.write(key, new Text(sb.deleteCharAt(sb.length() - 1).toString()));
    }
}

class FriendsMapperStepTwo extends Mapper<LongWritable, Text, Text, Text>
{
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String valueStr = value.toString();
        String commonFriend = valueStr.split("\t")[0];
        String[] hosts = valueStr.split("\t")[1].split(",");
        for (int i = 0; i < hosts.length - 1; i++)
        {
            for (int j = i + 1; j < hosts.length; j++)
            {
                context.write(new Text(sortRelation(hosts[i] + "," + hosts[j])),new Text(commonFriend));
            }
        }
    }

    private String sortRelation(String relation)
    {
        String[] friends = relation.split(",");
        Arrays.sort(friends);
        return (friends[0] + "," + friends[1]).intern();
    }
}

//
//class FriendsReducerStepTwo extends Reducer<Text, Text, Text, Text>
//{
//    @Override
//    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
//    {
//        StringBuilder sb = new StringBuilder();
//        for (Text value : values)
//        {
//            sb.append(value).append(",");
//        }
//        context.write(key, new Text(sb.deleteCharAt(sb.length() - 1).toString()));
//    }
//}
//
