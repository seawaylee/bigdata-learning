package nio;

/**
 * @author NikoBelic
 * @create 2017/4/11 22:00
 */
public class NioClient
{
    public static void main(String[] args)
    {
        int port = 8080;
        new NioClientTask(port).doTask();
    }
}
