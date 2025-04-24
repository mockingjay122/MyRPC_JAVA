package part1.Server.server.work;

import lombok.AllArgsConstructor;
import part1.Common.Message.RpcRequest;
import part1.Common.Message.RpcResponse;
import part1.Server.provider.ServiceProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable{

    private Socket socket;  //建立网络连接
    private ServiceProvider serviceProvider;  //本地服务注册中心


    @Override
    public void run() {
        try {
            //将response通过网络连接发送给客户端
            ObjectOutputStream oos =new ObjectOutputStream(socket.getOutputStream());
            //从客户端的网络连接接收数据
            ObjectInputStream ois =new ObjectInputStream(socket.getInputStream());
            //读取客户端传来的request
            RpcRequest rpcRequest =(RpcRequest) ois.readObject();
            //反射调用服务方法获取返回值
            RpcResponse rpcResponse = getResponse(rpcRequest);
            //向客户端发送响应信息
            oos.writeObject(rpcResponse);
            oos.flush();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    //处理客户端请求，根据请求内容调用对应服务的方法，并返回执行结构
    private RpcResponse getResponse(RpcRequest rpcRequest){
        //得到服务名
        String interfaceName = rpcRequest.getInterfaceName();
        //得到服务端相应的服务实现类
        Object service = serviceProvider.getService(interfaceName);
        //反射调用方法
        Method method=null;
        try {
            //获取方法对象
            method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamsType());
            Object invoke = method.invoke(service, rpcRequest.getParams());
            //封装响应对象返回
            return RpcResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }
}
