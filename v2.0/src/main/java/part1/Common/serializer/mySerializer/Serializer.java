package part1.Common.serializer.mySerializer;



/**
 *  用于为对象提供序列化和反序列化的功能
 *  通过一个静态工厂方法根据类型代码返回具体的序列化器实例
 */
public interface Serializer {
    // 把对象序列化成字节数组
    byte[] serialize(Object obj);
    // 从字节数组反序列化成消息, 使用java自带序列化方式不用messageType也能得到相应的对象（序列化字节数组里包含类信息）
    // 其它方式需指定消息格式，再根据message转化成相应的对象
    Object deserializer(byte[] bytes,int messageType);
    //返回使用的序列化器是哪个
    // 0 -> JAVA自带的序列化方式 1->JSON
    int getType();

    //静态工厂方法
    // 根据序号取出序列化器，暂时有两种实现方式，需要其它方式，实现这个接口即可
    static  Serializer getSerializerByCode(int code){
        switch (code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

}
