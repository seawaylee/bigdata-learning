package zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 分布式应用服务端主节点注册
 *
 * @author NikoBelic
 * @create 2017/3/30 17:21
 */
public class DistributedServer
{
    private static final String connStr = "hadoop1:8571,hadoop2:8572,hadoop3:8573";
    private static final String parentNode = "/servers";

    private ZooKeeper zkClient = null;

    private void connectToZk() throws IOException
    {
        zkClient = new ZooKeeper(connStr, 5000, (watchedEvent) ->
        {
            try
            {
                zkClient.getChildren(parentNode, false, null);
            } catch (KeeperException e)
            {
                e.printStackTrace();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
    }

    private void registServer(String serverName) throws KeeperException, InterruptedException
    {
        String created = zkClient.create(parentNode + "/" + serverName, serverName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(serverName + " is online.." + created);
    }

    private void handleBussiness(String serverName) throws InterruptedException
    {
        System.out.println(serverName + "start working...");
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException
    {
        // 获取zk连接
        DistributedServer distributedServer = new DistributedServer();
        String serverName = "server03";
        distributedServer.connectToZk();
        // 当一台服务器连接，则创建一个临时节点
        distributedServer.registServer(serverName );
        // 服务端开始处理业务
        distributedServer.handleBussiness(serverName );
    }
}
