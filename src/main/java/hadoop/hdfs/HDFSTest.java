package hadoop.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

/**
 * HDFS 基本API使用
 * @author NikoBelic
 * @create 2017/4/25 15:11
 */
public class HDFSTest
{
    FileSystem fs = null;
    Configuration conf = null;
    @Before
    public void init() throws IOException, URISyntaxException, InterruptedException
    {
        /*
         客户端去操作HDFS时，是有一个用户身份的
         默认情况下，HDFS客户端API会从JVM中获取一个参数来作为自己的用户身份，HADOOP_USER_NAME
         也可以在构建客户端FS对象时指定身份
          */
        conf = new Configuration();
        // 拿到一个文件系统操作的客户端实例对象
        fs = FileSystem.get(new URI("hdfs://10.5.151.241:9000"),conf,"root");
    }

    /**
     * 配置文件参数
     * @Author SeawayLee
     * @Date 2017/05/02 12:22
     */
    @Test
    public void testConfig()
    {
        Iterator<Map.Entry<String, String>> iterator = conf.iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, String> entry = iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    /**
     * 上传文件
     * @Author SeawayLee
     * @Date 2017/05/02 12:23
     */
    @Test
    public void testUpload() throws IOException, InterruptedException
    {
        fs.copyFromLocalFile(new Path("/Users/lixiwei-mac/百度云同步盘/MAC云存储/电子书/机器学习/机器学习-Mitchell-中文-清晰版.pdf"),new Path("/机器学习-Mitchell-中文-清晰版.pdf"));
        fs.close();
    }

    /**
     * 下载文件
     * @Author SeawayLee
     * @Date 2017/05/02 12:23
     */
    @Test
    public void testDownload() throws Exception
    {
        fs.copyToLocalFile(new Path("/paper.txt"),new Path("/Users/lixiwei-mac/Documents/IdeaProjects/bigdatalearning/src/data"));
        fs.close();
    }

    /**
     * 创建文件夹
     * @Author SeawayLee
     * @Date 2017/05/02 14:46
     */
    @Test
    public void testMkdir() throws IOException
    {
        boolean mkdirs = fs.mkdirs(new Path("/tesMkdir/aaa/bbb"));
        System.out.println(mkdirs);
    }

    /**
     * 删除文件或文件夹
     * @Author SeawayLee
     * @Date 2017/05/02 14:46
     */
    @Test
    public void testDelete() throws IOException
    {
        boolean delete = fs.delete(new Path("/tesMkdir"), true);
        System.out.println(delete);
    }

    /**
     * 查看文件信息 迭代方式
     * @Author SeawayLee
     * @Date 2017/05/02 14:46
     */
    @Test
    public void testLs() throws IOException
    {
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        while (listFiles.hasNext())
        {
            LocatedFileStatus file = listFiles.next();
            System.out.println("BlockSize:" + file.getBlockSize() / 1024.0 / 1024.0 + "MB");
            System.out.println("Owner:" + file.getOwner());
            System.out.println("Replication:" + file.getReplication());
            System.out.println("Permission:" + file.getPermission());
            System.out.println("Name:" + file.getPath().getName());
            BlockLocation[] blockLocations = file.getBlockLocations();
            System.out.println("");
            for (BlockLocation blockLocation : blockLocations)
            {
                System.out.println("Block-Name:");
                for (String s : blockLocation.getNames())
                {
                    System.out.print(s + " ");
                }
                System.out.println("");
                System.out.println("Block-Offset:" + blockLocation.getOffset());
                System.out.println("Block-Length:" + blockLocation.getLength());
                System.out.println("Block-Hosts:");
                for (String host : blockLocation.getHosts())
                {
                    System.out.print(host + "  ");
                }
                System.out.println("");
            }
            System.out.println("==========================");
        }

    }

    /**
     * 查看文件信息 数组方式
     * @Author SeawayLee
     * @Date 2017/05/02 14:46
     */
    @Test
    public void testLs2() throws IOException
    {
        FileStatus[] fileStatuses = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : fileStatuses)
        {
            System.out.println(fileStatus.getPath().getName());
        }
    }


}
