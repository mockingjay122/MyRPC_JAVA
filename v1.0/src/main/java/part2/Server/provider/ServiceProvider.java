package part2.Server.provider;

import java.util.HashMap;
import java.util.Map;

// 本地服务存放器
public class ServiceProvider {
    // 集合中存放服务的实例 ,接口的全限定名（String 类型）,接口对应的实现类实例（Object 类型）。
    private Map<String,Object> interfaceProvider;

    public ServiceProvider(){
        this.interfaceProvider =new HashMap<>();
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
            interfaceProvider.put(clazz.getName(),service);
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
