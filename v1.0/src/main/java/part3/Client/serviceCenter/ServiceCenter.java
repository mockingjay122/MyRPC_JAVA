package part3.Client.serviceCenter;

import java.net.InetSocketAddress;

//服务中心接口
public interface ServiceCenter {
    // 查询： 根据服务名查找地址 InetSocketAddress表示一个网络地址，包含IP地址和端口号 -》用于在网络中标识一个计算机端口
    InetSocketAddress serviceDiscovery(String serviceName);
}
