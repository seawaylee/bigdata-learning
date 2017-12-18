package web_click_mr_hive.hive.mr.pre;

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
import web_click_mr_hive.hive.mr_bean.WebLogBean;
import web_click_mr_hive.hive.mr_bean.WebLogParser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * 处理原始日志，过滤出真实PV请求
 * 转换时间格式
 * 对缺失字段填充默认值
 * 对记录标记valid和invalid
 *
 * @author NikoBelic
 * @create 2017/10/9 13:25
 */
public class WeblogPreProcess
{
    static class WebloProcessMapper extends Mapper<LongWritable, Text, Text, NullWritable>
    {
        private Set<String> pages = new HashSet<>();
        private Text k = new Text();
        NullWritable v = NullWritable.get();

        /**
         * URL分类数据，若添加静态文件过滤，则只有包含pages路径的请求属于合法数据
         *
         * @Author SeawayLee
         * @Date 2017/10/09 13:34
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException
        {
            pages.add("/about");
            pages.add("/black-ip-list/");
            pages.add("/cassandra-clustor/");
            pages.add("/finance-rhive-repurchase/");
            pages.add("/hadoop-family-roadmap/");
            pages.add("/hadoop-hive-intro/");
            pages.add("/hadoop-zookeeper-intro/");
            pages.add("/hadoop-mahout-roadmap/");
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
        {
            String line = value.toString();
            WebLogBean webLogBean = WebLogParser.parser(line);
            // 过滤js/img/css等静态资源
            WebLogParser.filterStaticResource(webLogBean, pages);
            if (!webLogBean.isValid())
                return;
            k.set(webLogBean.toString());
            context.write(k, v);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException
    {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(WeblogPreProcess.class);
        job.setMapperClass(WeblogPreProcess.WebloProcessMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);


        String inputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/data/web_click/pre/input";
        String outputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/data/web_click/pre/output";
        FileInputFormat.setInputPaths(job, new Path(inputPath));

        FileSystem fs = FileSystem.get(new URI(outputPath), conf, "root");
        if (fs.exists(new Path(outputPath)))
        {
            fs.delete(new Path(outputPath), true);
        }
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        job.setNumReduceTasks(0);
        job.waitForCompletion(true);
    }
}
