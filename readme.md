# 手写RPC框架

项目出自代码随想录，新手通过该项目可以更好理解RPC底层原理，作者是一名新手小白，该项目也是作者练手项目，并且项目仅供学习参考。

## v1.0

### part1

原生socket实现客户端远程调用服务端方法

### part2

在part1引入netty框架

### part3

在part2引入zookeeper作为服务注册中心

**测试流程：**

先执行服务端test，在执行客户端test

# v2.0

### part1

自定义编码器和解码器

### part2

在part1基础上增加本地缓存，使用zookeeper的watch机制进行监听，从而实现动态监听

## v3.0

### part1

通过一致性哈希实现负载均衡

### part2

使用Guava Retry 实现超时重试并在服务注册与发现实现白名单