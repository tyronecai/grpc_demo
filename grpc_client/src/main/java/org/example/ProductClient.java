// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.example;

import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.javaboy.grpc.demo.Product;
import org.javaboy.grpc.demo.ProductId;
import org.javaboy.grpc.demo.ProductInfoGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 代码来源 https://mp.weixin.qq.com/s/OyfU0tLm4f9t3nZxce-Ksw
public class ProductClient {
  private static final Logger LOG = LoggerFactory.getLogger(ProductClient.class);

  private static Options createOptions() {
    Options options = new Options();

    options.addOption("help", "usage help");
    options.addOption(
        Option.builder().hasArg(true).longOpt("host").type(String.class).desc("host").build());
    options.addOption(
        Option.builder().hasArg(true).longOpt("port").type(Short.class).desc("port").build());
    options.addOption(
        Option.builder()
            .hasArg(true)
            .longOpt("use_ssl")
            .type(Boolean.class)
            .desc("use ssl or not")
            .build());
    return options;
  }

  public static void main(String[] args) throws Exception {
    String host;
    int port;
    boolean useSSL;
    try {
      CommandLine commandLine = new DefaultParser().parse(createOptions(), args);
      host = commandLine.getOptionValue("host", "127.0.0.1");
      port = Integer.parseInt(commandLine.getOptionValue("port", "50051"));
      useSSL = Boolean.parseBoolean(commandLine.getOptionValue("use_ssl", "false"));
    } catch (Exception ex) {
      LOG.error("parse args fail, {}", ex.getMessage());
      LOG.error("Example: --host 127.0.0.1 --port 50011 --use_ssl false");
      throw ex;
    }

    LOG.info("will connect to server {}:{}, use ssl {}", host, port, useSSL);

    NettyChannelBuilder builder = NettyChannelBuilder.forAddress(host, port);
    // https://my.oschina.net/lenve/blog/7819695
    // 2. 客户端拦截器
    // 客户端拦截器就比较简单了，客户端拦截器可以将我们的请求拦截下来，例如我们如果想为所有请求添加统一的令牌 Token，那么就可以在这里来做，方式如下：
    builder.intercept(
        new ClientInterceptor() {
          @Override
          public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
              MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
            LOG.info("get intercept call {}", method.getFullMethodName());
            callOptions = callOptions.withAuthority("javaboy");
            return next.newCall(method, callOptions);
          }
        });

    if (useSSL) {
      // 忽略自签名证书的限制
      SslContext sslContext =
          GrpcSslContexts.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
      builder.sslContext(sslContext);
    } else {
      builder.usePlaintext();
    }

    ManagedChannel channel = builder.build();

    ProductInfoGrpc.ProductInfoBlockingStub stub = ProductInfoGrpc.newBlockingStub(channel);
    Product p =
        Product.newBuilder()
            .setId("1")
            .setPrice(399.0f)
            .setName("TienChin项目")
            .setDescription("SpringBoot+Vue3实战视频")
            .setTimestamp(System.currentTimeMillis())
            .build();

    ProductId productId = stub.addProduct(p);
    LOG.info("added productId {}", productId);

    Product product = stub.getProduct(ProductId.newBuilder().setValue("99999").build());
    LOG.info("get product {}", product);
  }
}
