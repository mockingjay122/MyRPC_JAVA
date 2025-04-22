package part3.Common.Service;

import part3.Common.pojo.User;

/**
 * 客户端调用服务接口
 */
public interface UserService {
    // client 通过这个接口调用服务端的实现类
    User getUserByUserId(Integer id);
    //新增一个功能
    Integer insertUserId(User user);
}
