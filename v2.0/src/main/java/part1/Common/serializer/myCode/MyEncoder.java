package part1.Common.serializer.myCode;

//MessageToByteEncoder是netty专门设计用来实现编码器得抽象类，
// 可以帮助开发者将Java对象编码成字节数据

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import part1.Common.Message.MessageType;
import part1.Common.Message.RpcRequest;
import part1.Common.Message.RpcResponse;
import part1.Common.serializer.mySerializer.Serializer;

@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder {
    private Serializer serializer;
    //netty在写出数据时会调用这个方法，将Java对象编码成二进制数据
    //参数ctx 是netty提供得上下文对象，代表管道上下文，包含通道和处理器相关信息。
    //参数msg是要编码得消息对象
    //参数out 是netty提供的字节缓冲区，编码后的字节数据写入其中

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //打印消息对象类名，用于调试编码过程中消息的类型
        System.out.println(msg.getClass());
        //判断消息是否是RpcRequest或RpcResponse类型，根据类型写入类型标识
        if(msg instanceof RpcRequest){
            out.writeShort(MessageType.REQUEST.getCode());
        }else if(msg instanceof RpcResponse){
            out.writeShort(MessageType.RESPONSE.getCode());
        }
        //写入当前序列化器的类型标识
        out.writeShort(serializer.getType());
        //将消息转化为字符数组
        byte[] serializeBytes = serializer.serialize(msg);
        //写入消息的字节长度
        out.writeInt(serializeBytes.length);
        //将字节数据内容写入输出缓冲去
        out.writeBytes(serializeBytes);
    }
}
