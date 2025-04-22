package part1.Client.proxy;

import lombok.AllArgsConstructor;
import part1.Client.IOClient;
import part1.Common.Message.RpcRequest;
import part1.Common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    // 传入参数service接口的class对象，反射封装为一个request
    private String host;
    private int port;

    //jdk动态代理，每一次代理对象调用方法，都会经过此方法增强（反射获取request对象，socket发送到服务端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RpcRequest request = RpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsType(method.getParameterTypes()).build();
        System.out.println(request);
        //IOClient.sendRequest 和服务端进行数据传输
        RpcResponse response = IOClient.sendRequest(host,port,request);
        return response.getData();
    }


    public <T>T getProxy(Class<T> clazz){
        /**
         * loader: clazz类加载器去加载代理对象
         * interfaces:动态代理类需要实现的接口
         * h:动态代理方法在执行时，会调用h里面的invoke方法去执行
         */
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
        return (T)o;
    }

}
