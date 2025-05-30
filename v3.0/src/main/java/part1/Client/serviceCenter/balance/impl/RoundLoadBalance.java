package part1.Client.serviceCenter.balance.impl;

import part1.Client.serviceCenter.balance.LoadBalance;

import java.util.List;

/**
 * 轮询实现负载均衡
 */
public class RoundLoadBalance implements LoadBalance {

    private int choose = -1;

    @Override
    public String balance(List<String> addressList) {
        choose++;
        choose=choose%addressList.size();
        System.out.println("负载均衡选择了"+choose+"服务器");
        return addressList.get(choose);
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
