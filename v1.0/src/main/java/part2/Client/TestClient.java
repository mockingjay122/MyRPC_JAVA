package part2.Client;


import part1.Common.Service.UserService;
import part1.Common.pojo.User;
import part2.Client.proxy.ClientProxy;

public class TestClient {
    public static void main(String[] args) {
        ClientProxy clientProxy =new ClientProxy("127.0.0.1",9999,0);
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user =proxy.getUserByUserId(1);
        System.out.println("从服务端得到的user="+user.toString());

        User u=User.builder().id(100).userName("wxx").sex(true).build();
        Integer id =proxy.insertUserId(u);
        System.out.println("向服务端插入user的id"+id);
    }
}
