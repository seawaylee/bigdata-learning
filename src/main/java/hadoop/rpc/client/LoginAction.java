package hadoop.rpc.client;

import hadoop.rpc.LoginServiceInterface;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author NikoBelic
 * @create 2017/5/2 16:16
 */
public class LoginAction
{
    public static void main(String[] args) throws IOException
    {
        LoginServiceInterface loginService = RPC.getProxy(LoginServiceInterface.class, 2L, new InetSocketAddress("localhost", 8888), new Configuration());
        String res = loginService.login("NikoBelic", "asdasd");
        System.out.println(res);
    }
}
