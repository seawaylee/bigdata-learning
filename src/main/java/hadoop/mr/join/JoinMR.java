package hadoop.mr.join;

import hadoop.mr.flowcount.FlowCount;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NikoBelic
 * @create 2017/5/22 21:33
 */
public class JoinMR
{
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, ClassNotFoundException
    {
        String inputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/join";
        String outputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/join/result";
        Configuration conf = new Configuration();
        //conf.set("mapreduce.framework.name", "yarn");
        //conf.set("yarn.resourcemanager.hostname", "hadoop1");

        Job job = Job.getInstance(conf);
        //job.setUser("NikoBelic");
        job.setJarByClass(FlowCount.class);

        job.setMapperClass(JoinMapper.class);
        job.setReducerClass(JoinReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(InfoBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(InfoBean.class);

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

class JoinMapper extends Mapper<LongWritable, Text, Text, InfoBean>
{
    InfoBean infobean = new InfoBean();
    Text keyout = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        String fileName = inputSplit.getPath().getName();
        String[] fields = value.toString().split(",");
        String productId;
        if (fileName.startsWith("order"))
        {
            productId = fields[2];
            infobean.setAll(fields[0], fields[1], fields[2], fields[3], "", "", "");
        } else
        {
            productId = fields[0];
            infobean.setAll("", "", "", "", fields[1], fields[2], fields[3]);
        }
        keyout.set(productId);
        context.write(keyout, infobean);
    }
}

class JoinReducer extends Reducer<Text, InfoBean, InfoBean, NullWritable>
{
    @Override
    protected void reduce(Text key, Iterable<InfoBean> values, Context context) throws IOException, InterruptedException
    {
        InfoBean productBean = new InfoBean();
        List<InfoBean> resultBeans = new ArrayList<>();
        for (InfoBean value : values)
        {
            if (!StringUtils.isBlank(value.getOrderId()))
            {
                try
                {
                    InfoBean orderBean = new InfoBean();
                    BeanUtils.copyProperties(orderBean,value);
                    resultBeans.add(orderBean);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else
            {
                try
                {
                    BeanUtils.copyProperties(productBean,value);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
        for (InfoBean bean : resultBeans)
        {
            bean.setProductName(productBean.getProductName());
            bean.setCategoryId(productBean.getCategoryId());
            bean.setPrice(productBean.getPrice());
            context.write(bean,NullWritable.get());
        }

    }
}