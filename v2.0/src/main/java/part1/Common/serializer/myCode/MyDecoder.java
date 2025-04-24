package part1.Common.serializer.myCode;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import part1.Common.Message.MessageType;
import part1.Common.serializer.mySerializer.Serializer;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    //它负责传入的字节流解码为业务对象，并将解码后的对象添加到out中，供下一个handler处理
    //ctx是Netty的ChannelHandlerContext对象，提供对管道、通道和事件的访问
    //in是ByteBuf对象，接收到的字节流，它是netty的缓冲区，可以理解为字节数组
    //out是List对象，用于存储解码后的对象
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //读取消息类型
        short messageType = in.readShort();
        //判断是否是请求或响应消息
        if(messageType!= MessageType.REQUEST.getCode()&&messageType!=MessageType.RESPONSE.getCode()){
            System.out.println("暂不支持此种数据");
        }
        //读取序列化类型
        Short serializerType = in.readShort();
        //获取对应的序列化对象，根据类型返回一个适当的序列化器
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        if(serializer==null){
            throw new RuntimeException("不存在对应的序列化器");
        }
        //读取消息长度和数据
        int length =in.readInt();
        byte[] bytes= new byte[length];
        //将消息内存序列化后存到字节数组中
        in.readBytes(bytes);
        //反序列化对象后赋值变量中
        Object deserialize = serializer.deserializer(bytes,messageType);
        //将对象添加到out列表中
        out.add(deserialize);
    }
}
