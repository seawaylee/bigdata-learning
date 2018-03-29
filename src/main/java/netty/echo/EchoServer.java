package netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author NikoBelic
 * @create 2018/2/11 13:52
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            // EchoServerHanlder被标注为@Shareable，所以我们可以总是使用同样的实例
                            channel.pipeline().addLast(serverHandler);
                        }
                    });
            // 异步绑定服务器; 调用sync()犯法阻塞等待直到绑定完成
            ChannelFuture future = sb.bind().sync();
            // 获取Channel的CloseFuture,并且阻塞当前线程直到他完成
            future.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup释放所有资源
            group.shutdownGracefully().sync();
        }
    }


    /**
     * 服务端 - ChannelHandler  - 业务逻辑处理
     * Sharable注解表示该Handler可以被多个Channel安全地共享
     */
    @ChannelHandler.Sharable
    static class EchoServerHandler extends ChannelInboundHandlerAdapter {

        /**
         * 对于每个传入的消息都要调用
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
            // 将接受到的消息写给发送者，而不冲刷出站消息
            ctx.write(in);
        }

        /**
         * 通知ChannelInboundHandler最后一次对channel-Read()的调用时当前批量读取中的最后一条哦消息
         */
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            // 将消息冲刷到远程节点，并且关闭该Channel
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }

        /**
         * 在读取操作期间，有异常抛出时会调用
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new EchoServer(9898).start();
    }
}




