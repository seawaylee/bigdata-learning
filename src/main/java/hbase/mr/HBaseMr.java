package hbase.mr;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NikoBelic
 * @create 2017/11/20 20:12
 */
public class HBaseMr
{
    static Configuration config = null;

    static
    {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "127.0.0.1");
        config.set("hbase.zookeeper.property.clientPort", "2181");
    }

    public static final String sourceTableName = "word";
    public static final String colf = "content";
    public static final String col = "info";
    public static final String targetTableName = "stat";

    public static void initTB()
    {
        HTable table;
        HBaseAdmin admin;
        try
        {
            admin = new HBaseAdmin(config);
            // 删除表
            if (admin.tableExists(sourceTableName) || admin.tableExists(targetTableName))
            {
                System.out.println("表已经存在了, 正在删除重建");
                admin.disableTable(sourceTableName);
                admin.deleteTable(sourceTableName);
                admin.disableTable(targetTableName);
                admin.deleteTable(targetTableName);
            }
            // 创建表
            HTableDescriptor sourceTableDesc = new HTableDescriptor(sourceTableName);
            HColumnDescriptor sourceTableCF = new HColumnDescriptor(colf);
            sourceTableDesc.addFamily(sourceTableCF);
            admin.createTable(sourceTableDesc);

            HTableDescriptor targetTableDesc = new HTableDescriptor(targetTableName);
            HColumnDescriptor targetTableCF = new HColumnDescriptor(colf);
            targetTableDesc.addFamily(targetTableCF);
            admin.createTable(targetTableDesc);

            // 插入数据
            table = new HTable(config, sourceTableName);
            table.setAutoFlush(false);
            table.setWriteBufferSize(5);
            List<Put> putList = new ArrayList<>();
            int lineCounter = 1;
            for (String line : FileUtils.readLines(new File("/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/data/paper.txt")))
            {
                putList.add(new Put(
                        Bytes.toBytes(String.valueOf(lineCounter++)))
                        .addColumn(Bytes.toBytes(colf), Bytes.toBytes(col), Bytes.toBytes(line)));

            }
            table.put(putList);
            table.flushCommits();
            putList.clear();
        } catch (MasterNotRunningException e)
        {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Mapper
     * ImmutableBytesWritable: 输入类型，表示rowkey
     * Text: 输出类型，表示单词
     * IntWritable: 输出类型，表示单词数量
     *
     * @Author SeawayLee
     * @Date 2017/11/21 17:07
     */
    public static class MyMapper extends TableMapper<Text, IntWritable>
    {
        private static IntWritable one = new IntWritable(1);
        private static Text word = new Text();

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException
        {
            //获取一行数据中的colf：col       表里面只有一个列族，所以就直接获取每一行的值
            String words = Bytes.toString(value.getValue(Bytes.toBytes(colf), Bytes.toBytes(col)));
            String wordArray[] = words.split(" ");
            for (String str : wordArray)
            {
                word.set(str);
                context.write(word, one);
            }
        }
    }

    /**
     * Reducer
     * Text: 输入的key类型
     * IntWritable: 输入的value类型
     * ImmutableBytesWritable: 输出类型，表示rowkey的类型
     *
     * @Author SeawayLee
     * @Date 2017/11/21 17:05
     */
    public static class MyReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable>
    {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
        {
            int sum = 0;
            for (IntWritable value : values)
            {
                sum += value.get();
            }
            Put put = new Put(Bytes.toBytes(key.toString()));
            put.addColumn(Bytes.toBytes(colf), Bytes.toBytes(col), Bytes.toBytes(String.valueOf(sum)));
            context.write(new ImmutableBytesWritable(Bytes.toBytes(key.toString())), put);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException
    {
        // 初始化表
        initTB();
        Job job = new Job(config, "HBaseMr");
        job.setJarByClass(HBaseMr.class);
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes(colf), Bytes.toBytes(col));
        // 配置Mapper、Reducer
        TableMapReduceUtil.initTableMapperJob(sourceTableName, scan, MyMapper.class, Text.class, IntWritable.class, job);
        TableMapReduceUtil.initTableReducerJob(targetTableName, MyReducer.class, job);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


}
