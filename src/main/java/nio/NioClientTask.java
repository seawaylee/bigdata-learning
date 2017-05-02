package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author NikoBelic
 * @create 2017/4/11 21:20
 */
public class NioClientTask
{
    private Selector selector;

    private SocketChannel sc;

    private volatile boolean stop;

    public NioClientTask(int port)
    {
        try
        {
            selector = Selector.open();
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("localhost", port));
            sc.configureBlocking(false);
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
            // 判断是否连接成功
            if (key.isConnectable())
            {
                if (sc.finishConnect())
                {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite(sc);
                } else
                {
                    System.exit(1);
                }
            }
            if (key.isReadable())
            {
                // 读取数据
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0)
                {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("客户端收到 回馈:" + body);
                    this.stop = true;
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

    public void doWrite(SocketChannel sc) throws IOException
    {
        byte[] bytes = "客户端第二次回馈 ".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        sc.write(writeBuffer);
    }
}
