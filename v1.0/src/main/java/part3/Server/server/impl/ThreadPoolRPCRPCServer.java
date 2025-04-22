package part3.Server.server.impl;

import part3.Server.provider.ServiceProvider;
import part3.Server.server.RpcServer;
import part3.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 对比SimpleRPCServer 有更好的高并发能力  -》避免每个请求都创建新的的线程
 */
public class ThreadPoolRPCRPCServer implements RpcServer {
    //定义线程池对象，用于管理和执行线程任务
    private final ThreadPoolExecutor threadPool;
    private ServiceProvider serviceProvider;
    //默认构造方法：创建线程池 核心线程等于CPU核心数，最大线程1000 非核心线程空闲存货时间60s，队列大小100
    public  ThreadPoolRPCRPCServer (ServiceProvider serviceProvider){
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),1000,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        this.serviceProvider =serviceProvider;
    }

    //自定义构造方法，允许用户自定义线程池参数
    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvider, int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue){
        threadPool =new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
        this.serviceProvider=serviceProvider;
    }


    @Override
    public void start(int port) {
        System.out.println("服务器启动了");
        try {
            ServerSocket serverSocket =new ServerSocket(port);
            while (true){
                Socket socket=serverSocket.accept();
                //使用线程池分发任务，每个客户端请求交给线程池管理
                threadPool.execute(new WorkThread(socket,serviceProvider));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}
