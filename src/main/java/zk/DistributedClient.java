package zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author NikoBelic
 * @create 2017/3/30 20:05
 */
public class DistributedClient
{
    private static final String connStr = "hadoop1:8571,hadoop2:8572,hadoop3:8573";
    private static final String parentNode = "/servers";
    private volatile List<String> servers;
    private ZooKeeper zkClient = null;


    private void connectToZk() throws IOException
    {
        zkClient = new ZooKeeper(connStr, 5000, (watchedEvent) ->
        {
            try
            {
                // 当接收到节点变化事件，重新获取服务器列表，并再次建立监听
                getServerList();
            } catch (KeeperException e)
            {
                e.printStackTrace();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取服务器子节点信息，并对父节点进行监听
     *
     * @Author SeawayLee
     * @Date 2017/03/30 20:09
     */
    private void getServerList() throws KeeperException, InterruptedException
    {
        List<String> children = zkClient.getChildren(parentNode, true);
        if (servers == null)
            servers = new ArrayList<>();
        servers.clear();

        for (String child : children)
        {
            byte[] data = zkClient.getData(parentNode + "/" + child, false, null);
            servers.add(new String(data));
        }

        // 打印服务器列表
        System.out.println(servers.toString());
    }

    private void handleBussiness() throws InterruptedException
    {
        System.out.println("Client start working...");
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException
    {
        DistributedClient distributedClient = new DistributedClient();
        distributedClient.connectToZk();

        distributedClient.getServerList();

        distributedClient.handleBussiness();
    }
}
