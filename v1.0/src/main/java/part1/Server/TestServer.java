package part1.Server;

import part1.Common.Service.UserService;
import part1.Common.Service.impl.UserServiceImpl;
import part1.Server.provider.ServiceProvider;
import part1.Server.server.RpcServer;
import part1.Server.server.impl.SimpleRPCServer;
import part1.Server.server.impl.ThreadPoolRPCRPCServer;

public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer=new SimpleRPCServer(serviceProvider);
        rpcServer.start(9999);
    }
}
