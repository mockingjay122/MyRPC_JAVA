package part1.Common.Message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageType {
    // 枚举常量，代表消息请求
    REQUEST(0),
    // 枚举常量，代表消息响应
    RESPONSE(1);
    //每个枚举值对应的编码
    private int code;
    //提供对code值得访问
    public int getCode(){
        return code;
    }
}
