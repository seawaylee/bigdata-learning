package nio;

/**
 * @author NikoBelic
 * @create 2017/4/11 21:17
 */
public class NioServer
{
    public static void main(String[] args)
    {
        int port = 8080;
        new NioServerTask(port).doTask();
    }
}
