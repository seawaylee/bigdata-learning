package hadoop.rpc.publish;

import hadoop.rpc.LoginServiceInterface;
import hadoop.rpc.impl.LoginServiceImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

/**
 * @author NikoBelic
 * @create 2017/5/2 16:05
 */
public class PublishServer
{
    public static void main(String[] args) throws IOException
    {
        RPC.Builder builder = new RPC.Builder(new Configuration());
        builder.setBindAddress("localhost")
                .setPort(8888)
                .setProtocol(LoginServiceInterface.class)
                .setInstance(new LoginServiceImpl());
        RPC.Server loginServer = builder.build();
        loginServer.start();
    }
}
