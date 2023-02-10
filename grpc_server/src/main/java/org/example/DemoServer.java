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

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import org.example.impl.BookServiceImpl;
import org.example.impl.ProductInfoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

// 代码来源 https://mp.weixin.qq.com/s/OyfU0tLm4f9t3nZxce-Ksw
public class DemoServer {
  private static final Logger LOG = LoggerFactory.getLogger(DemoServer.class);
  private static final String host = "127.0.0.1";
  private static final int port = 50051;
  private Server server;

  public static void main(String[] args) throws Exception {
    DemoServer server = new DemoServer();
    server.start();
    server.blockUntilShutdown();
  }

  // 使用命令生成证书
  // openssl req -newkey rsa:2048 -nodes -keyout server.key -x509 -days 365 -out server.crt
  public void start() throws IOException {
    File keyCertChainFile = new File("server.crt");
    File keyFile = new File("server.key");

    if (!keyCertChainFile.exists() || !keyFile.exists()) {
      LOG.error("crt & key file not exit");
      LOG.error(
          "use command "
              + "\"openssl req -newkey rsa:2048 -nodes -keyout server.key -x509 -days 365 -out server.crt\""
              + " to generate");
      throw new RuntimeException("bad crt & key file");
    }

    // 不强制client auth
    SslContext sslContext =
        GrpcSslContexts.forServer(keyCertChainFile, keyFile)
            .clientAuth(ClientAuth.OPTIONAL)
            .build();

    server =
        NettyServerBuilder.forAddress(new InetSocketAddress(host, port))
            .addService(new ProductInfoImpl())
            .addService(new BookServiceImpl())
            .sslContext(sslContext)
            .build()
            .start();

    LOG.info("server start on port {}", port);

    Runtime.getRuntime().addShutdownHook(new Thread(DemoServer.this::stop));
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
    LOG.info("server stop");
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }
}
