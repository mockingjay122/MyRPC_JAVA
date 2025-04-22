package part1.Client;

import part1.Common.Message.RpcRequest;
import part1.Common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {
    //这里负责底层与服务端的通信，发送request，返回response
    public static RpcResponse sendRequest(String host, int port, RpcRequest request){
        try {
            Socket socket = new Socket(host,port);
            // ObjectOutputStream 是一个用于将对象序列化成字节流的类
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 将request对象序列化并写入到输出流中
            oos.writeObject(request);
            oos.flush();
            // 读并反序列化
            RpcResponse response =(RpcResponse) ois.readObject();
            return response;
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

}
