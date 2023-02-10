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

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.javaboy.grpc.demo.Product;
import org.javaboy.grpc.demo.ProductId;
import org.javaboy.grpc.demo.ProductInfoGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;

// 代码来源 https://mp.weixin.qq.com/s/OyfU0tLm4f9t3nZxce-Ksw
public class ProductClient {
  private static final Logger LOG = LoggerFactory.getLogger(ProductClient.class);

  private static final String host = "127.0.0.1";
  private static final int port = 50051;

  public static void main(String[] args) throws SSLException {
    SslContext sslContext =
        GrpcSslContexts.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

    ManagedChannel channel =
        NettyChannelBuilder.forAddress(host, port).sslContext(sslContext).build();

    ProductInfoGrpc.ProductInfoBlockingStub stub = ProductInfoGrpc.newBlockingStub(channel);
    Product p =
        Product.newBuilder()
            .setId("1")
            .setPrice(399.0f)
            .setName("TienChin项目")
            .setDescription("SpringBoot+Vue3实战视频")
            .build();

    ProductId productId = stub.addProduct(p);
    LOG.info("added productId {}", productId);

    Product product = stub.getProduct(ProductId.newBuilder().setValue("99999").build());
    LOG.info("get product {}", product);
  }
}
