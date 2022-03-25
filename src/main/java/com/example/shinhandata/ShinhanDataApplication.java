package com.example.shinhandata;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;


@SpringBootApplication
public class ShinhanDataApplication {

    private static final int SERVER_PORT = 8086;
    private final ChannelGroup allChannels = new DefaultChannelGroup("server", GlobalEventExecutor.INSTANCE);
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;

    public void startServer(){
        bossEventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        workerEventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("worker"));

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossEventLoopGroup, workerEventLoopGroup);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.childHandler(new EchoServerInitializer());

        try{
            ChannelFuture bindFuture = bootstrap.bind(new InetSocketAddress(SERVER_PORT)).sync();
            Channel channel = bindFuture.channel();
            allChannels.add(channel);

            bindFuture.channel().closeFuture().sync();

        } catch (InterruptedException e){
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    private void close () {
        allChannels.close().awaitUninterruptibly();
        workerEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
        bossEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
    }

    public static void main(String[] args) throws Exception {
        String[] a = LocalDateTime.now().toString().split("T");
        a[0] = a[0].replace("-", ".");
        a[1] = a[1].substring(0,5);
        System.out.println(a[0] + " " + a[1]);
        new ShinhanDataApplication().startServer();
    }

}
