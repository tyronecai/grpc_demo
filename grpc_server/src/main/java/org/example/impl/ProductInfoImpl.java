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

import io.grpc.stub.StreamObserver;
import org.javaboy.grpc.demo.Product;
import org.javaboy.grpc.demo.ProductId;
import org.javaboy.grpc.demo.ProductInfoGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 代码来源 https://mp.weixin.qq.com/s/OyfU0tLm4f9t3nZxce-Ksw
public class ProductInfoImpl extends ProductInfoGrpc.ProductInfoImplBase {
  private static final Logger LOG = LoggerFactory.getLogger(ProductInfoImpl.class);

  @Override
  public void addProduct(Product request, StreamObserver<ProductId> responseObserver) {
    long timestamp = request.getTimestamp();
    LOG.info("add product {}, cost {} ms", request, System.currentTimeMillis() - timestamp);
    responseObserver.onNext(ProductId.newBuilder().setValue(request.getId()).build());
    responseObserver.onCompleted();
  }

  @Override
  public void getProduct(ProductId request, StreamObserver<Product> responseObserver) {
    LOG.info("get product {}", request);
    responseObserver.onNext(Product.newBuilder().setId(request.getValue()).setName("三国演义").build());
    responseObserver.onCompleted();
  }
}
