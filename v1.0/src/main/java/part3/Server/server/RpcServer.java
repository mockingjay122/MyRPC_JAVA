package part3.Server.server;

public interface RpcServer {
    //开启监听
    void start(int port);
    //停止服务端服务
    void stop();
}
