package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Netty客户端业务处理
 * @author NikoBelic
 * @create 2017/4/18 14:13
 */
public class EchoClientHandler extends SimpleChannelInboundHandler
{
    /**
     * 客户端连接服务器后被调用
     *
     * @Author SeawayLee
     * @Date 2017/04/18 14:15
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("客户端连接服务器，开始发送数据....");
        byte[] req = "Query Time Order".getBytes();
        ByteBuf firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        ctx.writeAndFlush(firstMessage);
    }


    /**
     * 从服务端接收到数据后调用
     *
     * @Author SeawayLee
     * @Date 2017/04/18 14:18
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception
    {
        System.out.println("Client 读取 Server 数据...");
        // 服务端返回消息后

        ByteBuf buf = (ByteBuf) o;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("服务端数据为：" + body);
    }

    /**
     * 发生异常时被调用
     *
     * @Author SeawayLee
     * @Date 2017/04/18 14:18
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        System.out.println("客户端发生异常...");
        cause.printStackTrace();
        ctx.close();
    }
}
