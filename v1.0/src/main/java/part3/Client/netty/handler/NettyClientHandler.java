package part3.Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import part3.Common.Message.RpcResponse;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    //这是 SimpleChannelInboundHandler 的核心方法，用于读取服务端返回的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        // 接收到response，给channel设计别名，让sendRequest里读取response
        // 将服务端返回的RpcResponse绑定到当前Channel的属性中，以便后续逻辑能够通过Channel获取该响应数据
        AttributeKey<RpcResponse> key=AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(response);
        //关闭当前channel （短链接模式）
        ctx.channel().close();
    }

    //用于捕获运行中出现的异常，进行处理并释放资源
    // 由于channel pipeline会依次向后传递 ，此处重写exceptionCaught 关闭上下文，减少资源浪费
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws  Exception{
        //异常处理
        cause.printStackTrace();
        ctx.close();
    }
}
