package zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableRowSorter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * ZooKeeper Java客户端API
 * @author NikoBelic
 * @create 2017/3/29 10:53
 */
public class SimpleZkClient
{
    private static final String connString = "hadoop1:2181,hadoop2:2181,hadoop3:2181";
    private static final int sessionTimeout = 2000;
    ZooKeeper zkClient = null;


    /**
     * 创建连接、监听回调函数
     * @Author SeawayLee
     * @Date 2017/03/29 13:52
     */
    @Before
    public void init() throws IOException
    {
        zkClient = new ZooKeeper(connString, sessionTimeout, (watchedEvent) ->
        {
            // 收到时间通知后的回调函数（事件处理逻辑）
            System.out.println(watchedEvent.getType() + "===" + watchedEvent.getPath());
            try
            {
                zkClient.getChildren("/", true);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    /**
     * 创建节点
     * @Author SeawayLee
     * @Date 2017/03/29 13:52
     */
    @Test
    public void testCreate() throws KeeperException, InterruptedException
    {
        // Params(创建节点的路径，节点数据，节点访问权限，节点类型)
        String nodeCreated = zkClient.create("/java_test2", "HelloWorld".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**获取所有节点
     *
     * @Author SeawayLee
     * @Date 2017/03/29 13:52
     */
    @Test
    public void getChildren() throws KeeperException, InterruptedException
    {
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children)
        {
            System.out.println(child);
        }
        TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断节点是否存在
     * @Author SeawayLee
     * @Date 2017/03/29 13:51
     */
    @Test
    public void testExist() throws KeeperException, InterruptedException
    {
        Stat exists = zkClient.exists("/java_test2", false);
        System.out.println(exists == null ? "节点不存在" : "节点存在");
    }

    /**
     * 获取节点
     * @Author SeawayLee
     * @Date 2017/03/29 13:51
     */
    @Test
    public void getData() throws KeeperException, InterruptedException
    {
        byte[] data = zkClient.getData("/java_test2", false, null);
        System.out.println(new String(data));
    }

    /**
     * 删除节点
     * @Author SeawayLee
     * @Date 2017/03/29 13:51
     */
    @Test
    public void deleteZnode() throws KeeperException, InterruptedException
    {
        // 参数2：指定要删除的版本，-1表示删除所有版本
        zkClient.delete("/java_test2", -1);
    }

    /**
     * 更新节点
     * @Author SeawayLee
     * @Date 2017/03/29 13:51
     */
    @Test
    public void updateZnode() throws KeeperException, InterruptedException
    {
        zkClient.setData("/test", "update test".getBytes(), -1);
        System.out.println(new String(zkClient.getData("/test", false, null)));
    }
}

