package part2.Client.proxy;


import part2.Client.RpcClient;
import part2.Client.impl.NettyRpcClient;
import part2.Client.retry.guavaRetry;
import part2.Client.serviceCenter.ServiceCenter;
import part2.Client.serviceCenter.ZKServiceCenter;
import part2.Common.Message.RpcRequest;
import part2.Common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 传入参数service接口的class对象，反射封装成一个request
// RPCClientProxy类中需要加入一个RPCClient类变量即可，传入不同的client（simple，netty），即可调用公共接口sendRequest发送请求
public class ClientProxy implements InvocationHandler {

    //传入参数service接口的class对象，反射封装成一个request
    private RpcClient rpcClient;

    private ServiceCenter serviceCenter;

    // 选择创建方式
    public ClientProxy() throws InterruptedException{
        serviceCenter = new ZKServiceCenter();
        rpcClient=new NettyRpcClient(serviceCenter);
    }

    public ClientProxy(String host,int port){
        rpcClient = new NettyRpcClient(host,port);
    }

    // 动态代理， 每一次代理对象调用方法，都会经过此方法增强 （反射获取request对象，socket发送到服务端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 构建request
        RpcRequest request = RpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName()).params(args).paramsType(method.getParameterTypes()).build();
        //数据传输
        RpcResponse response;
        //后续添加逻辑：为保持幂等性，只对白名单上的服务进行重试
        if (serviceCenter.checkRetry(request.getInterfaceName())){
            //调用retry框架进行重试操作
            response=new guavaRetry().sendServiceWithRetry(request,rpcClient);
        }else {
            //只调用一次
            response= rpcClient.sendRequest(request);
        }
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},this);
        return (T)o;
    }

}
