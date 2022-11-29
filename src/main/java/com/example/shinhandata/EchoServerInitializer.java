package com.example.shinhandata;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.util.List;

public class EchoServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        //pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

        pipeline.addLast(new ByteToMessageDecoder() {
            @Override
            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List <Object> out) throws Exception {
                out.add(in.readBytes(in.readableBytes()));
            }
        });

        //pipeline.addLast(new DelimiterBasedFrameDecoder()
        ByteBuf delimiter = Unpooled.copiedBuffer("$!".getBytes());
        pipeline.addLast(new DelimiterBasedFrameDecoder(4096, delimiter));
        //pipeline.addLast(new LineBasedFrameDecoder(4096));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new DiscardServerHandler());


    }
}