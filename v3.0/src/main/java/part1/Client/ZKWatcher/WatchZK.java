package part1.Client.ZKWatcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import part1.Client.cache.serviceCache;

public class WatchZK {
    // curator提供的zookeeper客户端
    private CuratorFramework client;
    //本地缓存
    serviceCache cache;

    public WatchZK(CuratorFramework client,serviceCache cache){
        this.client=client;
        this.cache=cache;
    }

    /**
     * 监听当前节点和子节点的更新、创建、删除
     */
    public void watchToUpdate(String path) throws  InterruptedException{
        CuratorCache curatorCache = CuratorCache.build(client,"/");
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            // 第一个参数：事件类型（枚举）
            // 第二个参数：节点更新前的状态、数据
            // 第三个参数：节点更新后的状态、数据
            // 创建节点时：节点刚被创建，不存在 更新前节点 ，所以第二个参数为 null
            // 删除节点时：节点被删除，不存在 更新后节点 ，所以第三个参数为 null
            // 节点创建时没有赋予值 create /curator/app1 只创建节点，在这种情况下，更新前节点的 data 为 null，获取不到更新前节点的数据
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                switch (type.name()){
                    case "NODE_CREATED": //监听器第一次执行时节点存在也会触发此事件
                        String [] pathList = pasrePath(childData1);
                        if(pathList.length<=2) break;
                        else{
                            String serviceName = pathList[1];
                            String address =pathList[2];
                            cache.addServiceToCache(serviceName,address);
                        }
                        break;
                    //节点更新
                    case "NODE_CHANGE":
                        if(childData.getData()!=null){
                            System.out.println("修改前的数据: "+new String(childData.getData()));
                        }else{
                            System.out.println("节点第一次赋值");
                        }
                        String[] oldPathList = pasrePath(childData);
                        String[] newPathList = pasrePath(childData1);
                        cache.replaceServiceAddress(oldPathList[1],oldPathList[2],newPathList[2]);
                        System.out.println("修改后的数据: " + new String(childData1.getData()));
                        break;
                    case "NODE_DELETED":
                        String [] PathList_d = pasrePath(childData);
                        if(PathList_d.length<=2) break;
                        else{
                            String serviceName= PathList_d[1];
                            String address = PathList_d[2];
                            //将新注册的服务从本地缓存中删除
                            cache.delete(serviceName,address);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //解析节点对应地址
    public String[] pasrePath(ChildData childData){
        //获取更新的节点的路径
        String path = new String(childData.getPath());
        //按照格式，读取
        return path.split("/");
    }
}
