package part1.Server.server.impl;

import part1.Server.provider.ServiceProvider;
import part1.Server.server.RpcServer;
import part1.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider; //本地注册中心


    @Override
    public void start(int port) {
        try {
            // 创建一个serverSocket 实例，用于指定的port端口上监听客户端的连接请求
            ServerSocket serverSocket =new ServerSocket(port);
            System.out.println("服务器启动了");
            while (true){
                // 如果没有连接，会堵塞到这里
                Socket socket =serverSocket.accept();
                // 有连接，创建一个新的线程执行处理
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}
