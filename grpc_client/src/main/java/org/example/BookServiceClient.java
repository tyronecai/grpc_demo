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

import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.grpc.stub.StreamObserver;
import org.javaboy.grpc.demo.Book;
import org.javaboy.grpc.demo.BookServiceGrpc;
import org.javaboy.grpc.demo.BookSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

// 代码引用于 https://mp.weixin.qq.com/s/c-_D2RpLksIlYJDfaWOSkA
public class BookServiceClient {
  private static final Logger LOG = LoggerFactory.getLogger(BookServiceClient.class);

  private static final String host = "127.0.0.1";
  private static final int port = 50051;

  public static void main(String[] args) throws Exception {
    SslContext sslContext =
        GrpcSslContexts.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

    ManagedChannel channel =
        NettyChannelBuilder.forAddress(host, port).sslContext(sslContext).build();

    BookServiceGrpc.BookServiceStub stub = BookServiceGrpc.newStub(channel);

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      try {
        System.out.println("Enter a action [addBook/getBook/searchBook/updateBook/processBook]:");
        String action = br.readLine();
        LOG.info("execute action {}", action);
        switch (action) {
          case "addBook":
            addBook(stub);
            break;
          case "getBook":
            getBook(stub);
            break;
          case "searchBook":
            searchBook(stub);
            break;
          case "updateBook":
            updateBook(stub);
            break;
          case "processBook":
            processBook(stub);
            break;
          default:
            LOG.warn("bad action {}", action);
            break;
        }
      } catch (Exception ex) {
        LOG.warn("get exception", ex);
        break;
      }
    }

    LOG.info("exit");
  }

  private static void addBook(BookServiceGrpc.BookServiceStub stub) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    stub.addBook(
        Book.newBuilder().setPrice(99).setId("100").setName("java").setAuthor("javaboy").build(),
        new StreamObserver<StringValue>() {
          @Override
          public void onNext(StringValue stringValue) {
            LOG.info("addBook onNext {}", stringValue.getValue());
          }

          @Override
          public void onError(Throwable throwable) {
            LOG.info("addBook onError");
          }

          @Override
          public void onCompleted() {
            countDownLatch.countDown();
            LOG.info("addBook onCompleted");
          }
        });
    countDownLatch.await();
  }

  private static void getBook(BookServiceGrpc.BookServiceStub stub) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    stub.getBook(
        StringValue.newBuilder().setValue("2").build(),
        new StreamObserver<Book>() {
          @Override
          public void onNext(Book book) {
            LOG.info("getBook onNext {}", book);
          }

          @Override
          public void onError(Throwable throwable) {
            LOG.info("getBook onError", throwable);
          }

          @Override
          public void onCompleted() {
            countDownLatch.countDown();
            LOG.info("getBook onCompleted");
          }
        });
    countDownLatch.await();
  }

  private static void searchBook(BookServiceGrpc.BookServiceStub stub) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    stub.searchBooks(
        StringValue.newBuilder().setValue("明清小说").build(),
        new StreamObserver<Book>() {
          @Override
          public void onNext(Book book) {
            LOG.info("searchBook onNext {}", book);
          }

          @Override
          public void onError(Throwable throwable) {
            LOG.info("searchBook onNext", throwable);
          }

          @Override
          public void onCompleted() {
            countDownLatch.countDown();
            LOG.info("searchBook onCompleted");
          }
        });
    countDownLatch.await();
  }

  private static void updateBook(BookServiceGrpc.BookServiceStub stub) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    StreamObserver<Book> request =
        stub.updateBooks(
            new StreamObserver<StringValue>() {
              @Override
              public void onNext(StringValue stringValue) {
                LOG.info("updateBook onNext {}", stringValue.getValue());
              }

              @Override
              public void onError(Throwable throwable) {
                LOG.info("updateBook onError");
              }

              @Override
              public void onCompleted() {
                countDownLatch.countDown();
                LOG.info("updateBook onCompleted");
              }
            });
    request.onNext(Book.newBuilder().setId("1").setName("a").setAuthor("b").build());
    request.onNext(Book.newBuilder().setId("2").setName("c").setAuthor("d").build());
    request.onCompleted();
    countDownLatch.await();
  }

  private static void processBook(BookServiceGrpc.BookServiceStub stub)
      throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    StreamObserver<StringValue> request =
        stub.processBooks(
            new StreamObserver<BookSet>() {
              @Override
              public void onNext(BookSet bookSet) {
                LOG.info("processBook onNext {}", bookSet);
              }

              @Override
              public void onError(Throwable throwable) {
                LOG.info("processBook onError");
              }

              @Override
              public void onCompleted() {
                countDownLatch.countDown();
                LOG.info("processBook onCompleted");
              }
            });
    request.onNext(StringValue.newBuilder().setValue("a").build());
    request.onNext(StringValue.newBuilder().setValue("b").build());
    request.onNext(StringValue.newBuilder().setValue("c").build());
    request.onNext(StringValue.newBuilder().setValue("d").build());
    request.onCompleted();
    countDownLatch.await();
  }
}
