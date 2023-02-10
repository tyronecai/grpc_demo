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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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
  private final String host;
  private final int port;
  private final boolean useSSL;
  private Server server;

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

    DemoServer server = new DemoServer(host, port, useSSL);
    server.start();
    server.blockUntilShutdown();
  }

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

  public DemoServer(String host, int port, boolean useSSL) {
    this.host = host;
    this.port = port;
    this.useSSL = useSSL;
  }

  // 使用命令生成证书
  // openssl req -newkey rsa:2048 -nodes -keyout server.key -x509 -days 365 -out server.crt
  public void start() throws IOException {
    NettyServerBuilder builder =
        NettyServerBuilder.forAddress(new InetSocketAddress(host, port))
            .addService(new ProductInfoImpl())
            .addService(new BookServiceImpl());

    LOG.info("server start on port {}:{}, use ssl {}", host, port, useSSL);

    if (useSSL) {
      LOG.info("use ssl");
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
      builder.sslContext(sslContext);
    }
    server = builder.build().start();

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
