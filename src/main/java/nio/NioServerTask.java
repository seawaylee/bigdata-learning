package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author NikoBelic
 * @create 2017/4/11 21:20
 */
public class NioServerTask
{
    private Selector selector;

    private ServerSocketChannel ssChannel;

    private volatile boolean stop;

    public NioServerTask(int port)
    {
        try
        {

            selector = Selector.open();
            ssChannel = ServerSocketChannel.open();
            ssChannel.configureBlocking(false);
            ssChannel.socket().bind(new InetSocketAddress(port), 1024);
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("THe time server starting in port " + port);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop()
    {
        this.stop = true;
    }

    public void doTask()
    {
        while (!stop)
        {
            try
            {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext())
                {
                    key = it.next();
                    it.remove();
                    try
                    {
                        handleInput(key);
                    } catch (Exception e)
                    {
                        if (key != null)
                        {
                            key.cancel();
                            if (key.channel() != null) key.channel().close();
                        }
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void handleInput(SelectionKey key) throws IOException
    {
        if (key.isValid())
        {
            // 处理新接入的请求信息
            if (key.isAcceptable())
            {
                // 接收新连接
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                // 向Selector注册新连接
                sc.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable())
            {
                // 读取数据
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0)
                {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("服务端接收到数据：" + body);
                    String currentTime = "服务端当前时间：" + new Date(System.currentTimeMillis()).toString();
                    doWrite(sc, currentTime);
                } else if (readBytes < 0)
                {
                    // 对端链路关闭
                    key.cancel();
                    sc.close();

                } else
                {
                    // 读到0字节 忽略
                }

            }
        }
    }

    public void doWrite(SocketChannel sc, String response) throws IOException
    {
        if (response != null && response.trim().length() > 0)
        {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            sc.write(writeBuffer);
        }
    }
}
