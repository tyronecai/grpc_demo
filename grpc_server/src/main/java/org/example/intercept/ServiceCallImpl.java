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

import io.grpc.ForwardingServerCall;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 代码来源 《聊一聊 gRPC 中的拦截器》 https://my.oschina.net/lenve/blog/7819695
public class ServiceCallImpl<ReqT, RespT>
        extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceCallImpl.class);
    protected ServiceCallImpl(ServerCall<ReqT, RespT> delegate) {
        super(delegate);
    }

    @Override
    public ServerCall<ReqT, RespT> delegate() {
        return super.delegate();
    }

    @Override
    public MethodDescriptor<ReqT, RespT> getMethodDescriptor() {
        return super.getMethodDescriptor();
    }

    @Override
    public void sendMessage(RespT message) {
        LOG.info("这是服务端返回给客户端的消息：{}", message);
        super.sendMessage(message);
    }
}
