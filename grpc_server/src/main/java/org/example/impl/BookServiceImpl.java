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

package org.example.impl;

import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;
import org.javaboy.grpc.demo.Book;
import org.javaboy.grpc.demo.BookServiceGrpc;
import org.javaboy.grpc.demo.BookSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 代码引用于 https://mp.weixin.qq.com/s/c-_D2RpLksIlYJDfaWOSkA
public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase {
  private static final Logger LOG = LoggerFactory.getLogger(BookServiceImpl.class);
  private final Map<String, Book> bookMap = new ConcurrentHashMap<>();

  public BookServiceImpl() {
    Book b1 =
        Book.newBuilder()
            .setId("1")
            .setName("三国演义")
            .setAuthor("罗贯中")
            .setPrice(30)
            .addTags("明清小说")
            .addTags("通俗小说")
            .build();
    Book b2 =
        Book.newBuilder()
            .setId("2")
            .setName("西游记")
            .setAuthor("吴承恩")
            .setPrice(40)
            .addTags("志怪小说")
            .addTags("通俗小说")
            .build();
    Book b3 =
        Book.newBuilder()
            .setId("3")
            .setName("水浒传")
            .setAuthor("施耐庵")
            .setPrice(50)
            .addTags("明清小说")
            .addTags("通俗小说")
            .build();
    bookMap.put("1", b1);
    bookMap.put("2", b2);
    bookMap.put("3", b3);
  }

  @Override
  public void addBook(Book request, StreamObserver<StringValue> responseObserver) {
    LOG.info("add book {}, cost {} ms", request, System.currentTimeMillis() - request.getTimestamp());
    bookMap.put(request.getId(), request);
    responseObserver.onNext(StringValue.newBuilder().setValue(request.getId()).build());
    responseObserver.onCompleted();
  }

  @Override
  public void getBook(StringValue request, StreamObserver<Book> responseObserver) {
    LOG.info("get book {}", request);
    String id = request.getValue();
    Book book = bookMap.get(id);
    if (book != null) {
      responseObserver.onNext(book);
      responseObserver.onCompleted();
    } else {
      responseObserver.onCompleted();
    }
  }

  // 服务器端流 RPC
  @Override
  public void searchBooks(StringValue request, StreamObserver<Book> responseObserver) {
    LOG.info("search book {}", request);
    Set<String> keySet = bookMap.keySet();
    String tags = request.getValue();
    for (String key : keySet) {
      Book book = bookMap.get(key);
      int tagsCount = book.getTagsCount();
      for (int i = 0; i < tagsCount; i++) {
        String t = book.getTags(i);
        if (t.equals(tags)) {
          responseObserver.onNext(book);
          break;
        }
      }
    }
    responseObserver.onCompleted();
  }

  // 客户端流 RPC
  @Override
  public StreamObserver<Book> updateBooks(StreamObserver<StringValue> responseObserver) {
    StringBuilder sb = new StringBuilder("更新的图书 ID 为：");
    return new StreamObserver<Book>() {
      @Override
      public void onNext(Book book) {
        LOG.info("updateBooks onNext {}, cost {} ms", book, System.currentTimeMillis() - book.getTimestamp());
        bookMap.put(book.getId(), book);
        sb.append(book.getId()).append(",");
      }

      @Override
      public void onError(Throwable throwable) {
        LOG.info("updateBooks error", throwable);
      }

      @Override
      public void onCompleted() {
        LOG.info("updateBooks completed");
        responseObserver.onNext(StringValue.newBuilder().setValue(sb.toString()).build());
        responseObserver.onCompleted();
      }
    };
  }

  // 双向流 RPC
  @Override
  public StreamObserver<StringValue> processBooks(StreamObserver<BookSet> responseObserver) {

    return new StreamObserver<StringValue>() {
      private final List<Book> books = new ArrayList<>();

      @Override
      public void onNext(StringValue stringValue) {
        LOG.info("processBooks onNext");

        Book b = Book.newBuilder().setId(stringValue.getValue()).build();
        books.add(b);
        if (books.size() == 3) {
          BookSet bookSet = BookSet.newBuilder().addAllBookList(books).build();
          responseObserver.onNext(bookSet);
          books.clear();
        }
      }

      @Override
      public void onError(Throwable throwable) {
        LOG.info("processBooks onError", throwable);
      }

      @Override
      public void onCompleted() {
        LOG.info("processBooks onCompleted");
        BookSet bookSet = BookSet.newBuilder().addAllBookList(books).build();
        responseObserver.onNext(bookSet);
        books.clear();
        responseObserver.onCompleted();
      }
    };
  }
}
