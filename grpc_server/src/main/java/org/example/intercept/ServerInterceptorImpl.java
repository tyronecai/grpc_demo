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

package org.example.intercept;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

// 代码来源 《聊一聊 gRPC 中的拦截器》 https://my.oschina.net/lenve/blog/7819695
public class ServerInterceptorImpl implements ServerInterceptor {
  private static final Logger LOG = LoggerFactory.getLogger(ServerInterceptorImpl.class);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    StringBuilder sb = new StringBuilder();
    sb.append("full method name: \"")
        .append(call.getMethodDescriptor().getFullMethodName())
        .append("\"");
    sb.append("\n");
    sb.append("headers: ");
    sb.append("\n");
    for (String key : headers.keys()) {
      sb.append(key);
      sb.append(": ");
      sb.append(headers.get(Metadata.Key.of(key, ASCII_STRING_MARSHALLER)));
      sb.append("\n");
    }
    LOG.info("get request {}", sb);
    return new ServiceCallListenerImpl(next.startCall(new ServiceCallImpl(call), headers));
  }
}
