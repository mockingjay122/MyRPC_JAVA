package part3.Client.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import part3.Client.serviceCenter.ServiceCenter;
import part3.Client.serviceCenter.ZKServiceCenter;
import part3.Common.Message.RpcRequest;
import part3.Common.Message.RpcResponse;
import part3.Client.RpcClient;
import part3.Client.netty.nettyInitializer.NettyClientInitializer;

import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {
    private String host;
    private int port;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private ServiceCenter serviceCenter;

    public NettyRpcClient(){
        this.serviceCenter=new ZKServiceCenter();
    }

    public NettyRpcClient(String host ,int part){
        this.host=host;
        this.port=port;
    }

    // netty 客户端初始化
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap =new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
    }


    @Override
    public RpcResponse sendRequest(RpcRequest request) {

        // 从注册中心获取host，post
        InetSocketAddress address = serviceCenter.serviceDiscovery(request.getInterfaceName());
        String host = address.getHostName();
        int port =address.getPort();
        try {
            // 创建一个操作事件，sync方法表示堵塞知道connect走完
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            // channel 表示一个连接的单位
            Channel channel =channelFuture.channel();
            //发送数据
            channel.writeAndFlush(request);
            //sync堵塞获取结果
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 当前场景下选择堵塞获取结果
            // 其它场景也可以选择添加监听器的方式来异步获取结果 channelFuture.addListener...
            AttributeKey<RpcResponse> key =AttributeKey.valueOf("RPCResponse");
            RpcResponse response=channel.attr(key).get();
            System.out.println(response);
            return response;

        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
}
