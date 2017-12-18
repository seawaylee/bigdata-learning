package pre_course.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Socket客户端
 * @author NikoBelic
 * @create 2017/4/10 21:02
 */
public class SocketClient
{
    public static void main(String[] args) throws IOException
    {
        // 创建套接字，与服务端进行通讯
        Socket socket = new Socket("localhost", 8899);
        // 从套接字中获得输出流（向服务端发送的数据流）
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(outputStream);
        // 从套接字中获得输入流（服务端回传给客户端的数据流）
        InputStream inputStream = socket.getInputStream();
        Scanner scanner = new Scanner(System.in);
        String input;
        while (!(input = scanner.nextLine()).equals("end"))
        {
            // 向服务端发送数据流
            pw.println(input);
            pw.flush();

            // 读取端返回的处理结果
            System.out.println(new BufferedReader(new InputStreamReader(inputStream)).readLine());
        }
    }
}
