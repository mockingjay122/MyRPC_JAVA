package part1.Client.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import part1.Client.netty.handler.NettyClientHandler;
import part1.Common.serializer.myCode.MyDecoder;
import part1.Common.serializer.myCode.MyEncoder;
import part1.Common.serializer.mySerializer.JsonSerializer;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 初始化 ，每个SocketChannel都有一个独立的管道pipeline 用于定义数据的处理流程
        ChannelPipeline pipeline =ch.pipeline();
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new NettyClientHandler());
    }
}
