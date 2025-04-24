package part2.Server;


import part2.Common.Service.UserService;
import part2.Common.Service.impl.UserServiceImpl;
import part2.Server.provider.ServiceProvider;
import part2.Server.server.RpcServer;
import part2.Server.server.impl.NettyRPCRPCServer;

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider("127.0.0.1",9999);
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer= new NettyRPCRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}
