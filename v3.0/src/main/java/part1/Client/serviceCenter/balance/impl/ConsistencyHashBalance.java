package part1.Client.serviceCenter.balance.impl;

import part1.Client.serviceCenter.balance.LoadBalance;

import java.util.*;

/**
 * 一致性哈希算法
 */
public class ConsistencyHashBalance implements LoadBalance {

    //虚拟节点的个数
    private static  final  int VIRTUAL_NUM = 5;

    // 虚拟节点分配，key是hash值，value是虚拟节点服务器名称
    private SortedMap<Integer,String> shards = new TreeMap<Integer, String>();

    // 真实节点列表
    private List<String> realNodes = new LinkedList<String>();

    // 模拟初始服务器
    private String[] servers = null;

    // 初始化负载均衡器 将真实的服务结点和对应的虚拟结点添加到哈希环中
    private void init(List<String> serviceList){
        for(String server:serviceList){
            realNodes.add(server);
            System.out.println("真实节点[" + server + "] 被添加");
            // 每个真实结点都会生成VIRTUAL_NUM个虚拟结点，并计算他们的哈希值
            for(int i = 0 ;i < VIRTUAL_NUM ; i++){
                String virtualNode = server + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash,virtualNode);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被添加");
            }
        }
    }

    // 根据请求的node 比如某个请求的标识符，选择一个服务器节点
    public String getServer(String node,List<String> serviceList){
        // 首先调用init 初始化哈希环
        init(serviceList);
        int hash = getHash(node);
        Integer key = null;
        // 获取hash值大于等于请求哈希值的所有虚拟节点
        SortedMap<Integer,String> subMap = shards.tailMap(hash);
        //如果没有找到，意味着请求的哈希值大于所有虚拟节点的哈希值，选择哈希值最大的虚拟节点
        //否则选择tailMap中第一个虚拟节点
        if(subMap.isEmpty()){
            key = shards.lastKey();
        }else{
            key = subMap.firstKey();
        }
        // 返回真实节点，从选中的虚拟节点中提取出真实节点的名称
        String virtualNode = shards.get(key);
        return virtualNode.substring(0,virtualNode.indexOf("&&"));
    }

    // 添加一个新的真实节点及其虚拟节点到哈希环中
    @Override
    public void addNode(String node) {
        if(!realNodes.contains(node)){
            realNodes.add(node);
            System.out.println("真实节点[" + node + "] 上线添加");
            // 每个真实结点都会生成VIRTUAL_NUM个虚拟结点，并计算他们的哈希值
            for(int i = 0 ;i < VIRTUAL_NUM ; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被添加");
            }
        }
    }

    // 删除一个真实节点及其虚拟节点
    @Override
    public void delNode(String node) {
        if(!realNodes.contains(node)){
            realNodes.remove(node);
            System.out.println("真实节点[" + node + "] 下线移除");
            // 每个真实结点都会生成VIRTUAL_NUM个虚拟结点，并计算他们的哈希值
            for(int i = 0 ;i < VIRTUAL_NUM ; i++) {
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
                System.out.println("虚拟节点[" + virtualNode + "] hash:" + hash + "，被移除");
            }
        }
    }

    // FNV1_32_HASH算法
    //该方法用于计算字符串的哈希值，哈希算法基于一个常见的算法，使用了 FNV-1a 哈希和一些额外的位运算，确保哈希值均匀分布
    private static int getHash(String str){
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }

    // 模拟负载均衡，通过生成一个随机字符串来模拟请求，最终通过一致性哈希选择一个服务器
    @Override
    public String balance(List<String> addressList) {
        String random = UUID.randomUUID().toString();
        return getServer(random,addressList);
    }

}
