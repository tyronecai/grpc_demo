package org.example;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cai.rong@yottabyte.cn
 * @date 2023-02-23 13:16
 */
public class ClientInterceptorImpl implements ClientInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(ClientInterceptorImpl.class);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        LOG.info("get intercept call {}", method.getFullMethodName());
        callOptions = callOptions.withAuthority("javaboy");
        return next.newCall(method, callOptions);
    }
}
