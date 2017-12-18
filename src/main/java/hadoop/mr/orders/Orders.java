package hadoop.mr.orders;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;

/**
 * @author NikoBelic
 * @create 2017/6/4 22:18
 */
public class Orders
{
    public static void main(String[] args) throws Exception
    {
        String inputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/orders";
        String outputPath = "/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/main/java/hadoop/data/orders/result";
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        //job.setUser("NikoBelic");
        //job.setJarByClass(FlowCount.class);

        job.setMapperClass(OrdersMapper.class);
        job.setReducerClass(OrdersReducer.class);
        job.setPartitionerClass(OrdersPartitioner.class);
        job.setNumReduceTasks(3);

        job.setMapOutputKeyClass(OrderDO.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(OrderDO.class);
        job.setOutputValueClass(NullWritable.class);

        job.setGroupingComparatorClass(OrderIdGroupingComparator.class);

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


class OrdersMapper extends Mapper<LongWritable, Text, OrderDO, NullWritable>
{
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String[] fields = value.toString().split(",");
        context.write(new OrderDO(fields[0], fields[1], Double.valueOf(fields[2])), NullWritable.get());

    }
}

class OrdersPartitioner extends Partitioner<OrderDO, NullWritable>
{
    @Override
    public int getPartition(OrderDO orderDO, NullWritable nullWritable, int i)
    {
        System.out.println((orderDO.getOrderNo().hashCode() & Integer.MAX_VALUE) % i);
        return (orderDO.getOrderNo().hashCode() & Integer.MAX_VALUE) % i;
    }
}

class OrdersReducer extends Reducer<OrderDO, NullWritable, OrderDO, NullWritable>
{
    @Override
    protected void reduce(OrderDO key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException
    {
        context.write(key, NullWritable.get());
    }
}

class OrderIdGroupingComparator extends WritableComparator
{
    public OrderIdGroupingComparator()
    {
        super(OrderDO.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b)
    {
        OrderDO o1 = (OrderDO) a;
        OrderDO o2 = (OrderDO) b;
        return o1.getOrderNo().compareTo(o2.getOrderNo());

    }
}

class OrderDO implements WritableComparable<OrderDO>
{
    private String orderNo;
    private String productName;
    private Double price;

    public OrderDO()
    {
    }

    public OrderDO(String orderNo, String productName, Double price)
    {
        this.orderNo = orderNo;
        this.productName = productName;
        this.price = price;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    @Override
    public int compareTo(OrderDO o)
    {
        if (this.orderNo.compareTo(o.getOrderNo()) == 0)
        {
            return -this.price.compareTo(o.getPrice());
        } else
        {
            return this.orderNo.compareTo(o.getOrderNo());
        }
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException
    {
        dataOutput.writeUTF(this.orderNo);
        dataOutput.writeUTF(this.productName);
        dataOutput.writeDouble(this.price);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException
    {
        this.orderNo = dataInput.readUTF();
        this.productName = dataInput.readUTF();
        this.price = dataInput.readDouble();
    }

    @Override
    public String toString()
    {
        return "OrderDO{" + "orderNo='" + orderNo + '\'' + ", productName='" + productName + '\'' + ", price=" + price + '}';
    }
}