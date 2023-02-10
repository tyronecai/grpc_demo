# grpc_demo

## 代码来源于《江南一点雨》的文章:
[一个简单的案例入门 gRPC](https://mp.weixin.qq.com/s/OyfU0tLm4f9t3nZxce-Ksw)
[聊一聊 gRPC 的四种通信模式](https://mp.weixin.qq.com/s/c-_D2RpLksIlYJDfaWOSkA)

并做了如下修改：
1. 支持ssl自签名证书
2. 日志通过reload4j输出
3. client支持运行中输入指令

## 编译
使用mvn package编译打包为fat jar
```shell
mvn clean package
```

## 使用
启动server
```shell
java -cp grpc_server/target/grpc_server-1.0-SNAPSHOT.jar: org.example.DemoServer
```

启动client
```shell
java -cp grpc_client/target/grpc_client-1.0-SNAPSHOT.jar: org.example.ProductClient
```
```shell
java -cp grpc_client/target/grpc_client-1.0-SNAPSHOT.jar: org.example.BookServiceClient
```
