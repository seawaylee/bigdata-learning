package socket;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Socket服务端
 * @author NikoBelic
 * @create 2017/4/10 20:51
 */
public class SocketServer
{
    public static void main(String[] args) throws IOException
    {
        // 创建Socket服务端，绑定到本地8899端口
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost", 8899));
        // 使用线程池异步处理业务逻辑（否则将不支持多客户端）
        ExecutorService threadPool = new ThreadPoolExecutor(0, 3,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(2));
        Socket socket;
        while (true)
        {
            // 接收客户端请求（阻塞方法）
            socket = serverSocket.accept();
            // 创建线程 处理业务逻辑
            threadPool.execute(new SocketTask(socket));
        }
    }
}
