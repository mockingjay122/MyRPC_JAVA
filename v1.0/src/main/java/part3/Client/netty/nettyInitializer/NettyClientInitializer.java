package part3.Client.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import part3.Client.netty.handler.NettyClientHandler;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 初始化 ，每个SocketChannel都有一个独立的管道pipeline 用于定义数据的处理流程
        ChannelPipeline pipeline =ch.pipeline();
        // 消息格式 长度 + 消息体 解决沾包问题
        /*
        参数含义：
        Integer.MAX_VALUE：允许的最大帧长度。
        0, 4：表示长度字段的起始位置和长度。
        0, 4：去掉长度字段后，计算实际数据的偏移量。
         */
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        //计算当前待发送消息的长度，写入到前4格字节中
        pipeline.addLast(new LengthFieldPrepender(4));
        // 编码器
        // 使用java序列化方式，neety的自带的编码支持传输这种结构
        pipeline.addLast(new ObjectEncoder());
        // 解码器
        // 使用Netty中的ObjectDecoder 用于将字节流解码为java对象
        // classResolver用于解析类名并加载响应的类
        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));

        pipeline.addLast(new NettyClientHandler());
    }
}
