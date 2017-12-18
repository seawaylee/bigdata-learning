package pre_course.socket;

import java.io.*;
import java.net.Socket;

/**
 * 业务逻辑处理线程
 * @author NikoBelic
 * @create 2017/4/10 21:16
 */
public class SocketTask implements Runnable
{
    private Socket socket;

    public SocketTask(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        InputStream in = null;
        OutputStream out = null;
        PrintWriter pw;
        BufferedReader reader;
        String clientStrs;
        try
        {
            // 从套接字中获取输入流（客户端传输的数据流）
            in = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            // 从套接字中获取输出流（回传给客户端的数据流）
            out = socket.getOutputStream();
            pw = new PrintWriter(out);
            // 从输入流中读取数据（注意：readline是阻塞方法）
            while ((clientStrs = reader.readLine()) != null)
            {
                System.out.println("Server端接收到:" + clientStrs);
                // 给客户端回传的内容
                pw.println("我收到了:" + clientStrs);
                pw.flush();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }finally
        {
            try
            {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}
