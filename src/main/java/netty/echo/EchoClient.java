package netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author NikoBelic
 * @create 2018/2/11 13:53
 */
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture future = bs.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    /**
     * 客户端ChannelHandler
     * 为什么继承了SimpleChannelInboundHandler而不是ChannelInboundHandlerAdapter？
     * 在客户端，当channelRead0()方法完成时，你已经有了传入消息，并且已经处理完它了。
     * 当该方法返回时，SimpleChannelInboundHandler负责释放指向保存该消息的ByteBuf的内存引用。
     * 在EchoServerHandler中，你仍然需要将传入消息回送给发送者，而write()操作是异步的，直到channelRead()方法返回后可能仍然没有完成。
     * 为此，EchoServerHandler扩展了ChannelInboundHandlerAdapter，其在这个时间点上不会释放消息。
     * 消息在EchoServerHandler的channelReadComplete()方法中，当writeAndFlush()方法被调用时被释放
     */
    @ChannelHandler.Sharable
    static class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
        /**
         * 与服务器的连接建立之后将被调用
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // 当被通知Channel是活跃的时候，发送一条消息
            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        }

        /**
         * 当从服务器接收到一条消息时被调用
         */
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
            System.out.println("Client Received: " + byteBuf.toString(CharsetUtil.UTF_8));
        }

        /**
         * 发生异常时调用
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            // 关闭Channel
            ctx.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient("127.0.0.1", 9898).start();
    }
}
