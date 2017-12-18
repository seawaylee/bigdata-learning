package zk.book;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author NikoBelic
 * @create 2017/6/7 15:23
 */
public class ZkClientTest
{
    ZkClient zkClient = null;

    @Before
    public void init()
    {
        zkClient = new ZkClient("localhost:2181");
    }

    @Test
    public void testCreateNode()
    {
        String path = "/lxw/c";
        String data = "lxw znode";
        zkClient.create(path, data, CreateMode.PERSISTENT);
        System.out.println(zkClient.readData(path).toString());
    }

    @Test
    public void testWaching() throws InterruptedException
    {
        String path = "/lxw";
        zkClient.subscribeChildChanges(path, (s, list) ->
        {
            System.out.println(s + " Child Changes To " + list);
        });
        Thread.sleep(5 * 1000 * 1000);
    }
}
