package part3.Client;

import part3.Common.Message.RpcRequest;
import part3.Common.Message.RpcResponse;

public interface RpcClient {

    // 共性抽取出来，定义底层通信的方法
    RpcResponse sendRequest(RpcRequest request);
}
