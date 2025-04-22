package part3.Server.provider;

import part3.Server.serviceRegister.ServiceRegister;
import part3.Server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

// 本地服务存放器
public class ServiceProvider {
    // 集合中存放服务的实例 ,接口的全限定名（String 类型）,接口对应的实现类实例（Object 类型）。
    private Map<String,Object> interfaceProvider;
    private int port;
    private String host;

    // 注册服务类
    private ServiceRegister serviceRegister;

    public ServiceProvider(String host,int port){
        //需要传入服务端自身的网络地址
        this.host=host;
        this.port=port;
        this.interfaceProvider=new HashMap<>();
        this.serviceRegister=new ZKServiceRegister();
    }

    /**
     * @brief：将服务实例注册到interfaceProvider，并于其实现的接口关联起来
     * service -》 实现了接口userservice和orderservice -》 userservice和orderservice为key，service为value
     */
    //本地注册服务
    public void provideServiceInterface(Object service){
        String serviceName =service.getClass().getName();
        Class<?> [] interfaceName =service.getClass().getInterfaces();

        for(Class<?> clazz:interfaceName){
            // 本机的映射表
            interfaceProvider.put(clazz.getName(),service);
            // 在注册中心注册服务
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port));
        }

//        interfaceProvider.forEach((key,value)->{
//            System.out.println(key + " " + value);
//        });
    }

    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }


}
