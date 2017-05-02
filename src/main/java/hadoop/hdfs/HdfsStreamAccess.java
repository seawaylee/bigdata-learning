package hadoop.hdfs;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * 流方式操作HDFS
 * @author NikoBelic
 * @create 2017/5/2 13:31
 */
public class HdfsStreamAccess
{
    FileSystem fs = null;
    Configuration conf = null;

    @Before
    public void init() throws IOException, URISyntaxException, InterruptedException
    {
        conf = new Configuration();
        fs = FileSystem.get(new URI("hdfs://10.5.151.241:9000"), conf, "root");
    }

    /**
     * 通过流的方式上传文件
     * @Author SeawayLee
     * @Date 2017/05/02 13:41
     */
    @Test
    public void testUpload() throws IOException
    {
        FSDataOutputStream outputStream = fs.create(new Path("/linux系统安装过程.avi"));
        FileInputStream inputStream = new FileInputStream("/Users/lixiwei-mac/linux系统安装过程.avi");
        IOUtils.copy(inputStream, outputStream);

    }

    /**
     * 通过流的方式下载文件
     * @Author SeawayLee
     * @Date 2017/05/02 13:55
     */
    @Test
    public void testDownload() throws IOException
    {
        FSDataInputStream inputStream = fs.open(new Path("/TreeFor.png"));
        FileOutputStream outputStream = new FileOutputStream("/Users/lixiwei-mac/Downloads/fuck.txt");
        IOUtils.copy(inputStream, outputStream);
    }

    /**
     * 通过流的方式指读取文件大小(MapReduce从HDFS读文件进行切片时肯定会用到)
     * @Author SeawayLee
     * @Date 2017/05/02 14:18
     */
    @Test
    public void testRandomAccess() throws IOException
    {
        FSDataInputStream inputStream = fs.open(new Path("/TreeFor.png"));
        inputStream.seek(50);
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader bf = new BufferedReader(isr);
        char[] buffer = new char[1024 * 20];
        int len = 0;
        while ((len = bf.read(buffer) )!= -1)
        {
            System.out.print(String.valueOf(buffer,0,len));
        }
    }

    /**
     * 流的方式读取文件
     * @Author SeawayLee
     * @Date 2017/05/02 14:21
     */
    @Test
    public void testCat() throws IOException
    {
        FSDataInputStream inputStream = fs.open(new Path("/TreeFor.png"));
        IOUtils.copy(inputStream, System.out);
    }
}

