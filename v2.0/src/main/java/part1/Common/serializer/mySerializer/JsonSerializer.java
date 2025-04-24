package part1.Common.serializer.mySerializer;

import com.alibaba.fastjson.JSONObject;
import org.checkerframework.checker.units.qual.C;
import part1.Common.Message.RpcRequest;
import part1.Common.Message.RpcResponse;

public class JsonSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj) {
        //将对象转化为json格式的字符数组
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }

    @Override
    public Object deserializer(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType){
            case 0:
                //将字节数组转化为RpcRequest对象
                RpcRequest request = JSONObject.parseObject(bytes,RpcRequest.class);
                //创建一个Object类型的数组，用于存储解析后的请求参数
                Object[] objects = new Object[request.getParamsType().length];
                //fastjson可以读出基本数据类型，不用转化
                //对转化后的request的params属性进行类型判断
                for(int i =0;i<objects.length;i++){
                    //paramsType是目标参数类型，request.getParamsType()[i]是类型数组，每个元素表示参数目标类型
                    //由RPC框架在调用方法时动态决定
                    Class<?> paramsType = request.getParamsType()[i];
                    // 如果类型兼容，则直接赋值，否则使用fastjson进行类型转换
                    if(!paramsType.isAssignableFrom(request.getParams()[i].getClass())){
                        objects[i] =JSONObject.toJavaObject((JSONObject)request.getParams()[i],request.getParamsType()[i]);
                    }else{
                        objects[i] = request.getParams()[i];
                    }
                }
                //将转换后的参数列表赋值回request对象的params属性
                //这样，RpcRequest对象的params就是一个与paramsType对应的、类型正确的参数数组。
                request.setParams(objects);
                obj =request;
                break;
            case 1:
                //将字节数组转化为RpcResponse对象
                RpcResponse response = JSONObject.parseObject(bytes, RpcResponse.class);
                //根据对象获取对象类型
                Class<?> dataType = response.getDataType();
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject)response.getData(),dataType));
                }

                obj =response;
                break;
            default:
                System.out.println("暂时不支持这种消息");
                throw new RuntimeException();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 1;
    }
}
