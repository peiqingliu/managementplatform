package com.wuqianqian.springcloud.gateway.fallback;

import com.wuqianqian.core.commons.constants.MessageConstant;
import com.wuqianqian.core.commons.constants.ServiceIdConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 在前面学习hystrix的时候，我们知道hystrix有fallback回退能力，及如果服务调用出现了异常，则执行指定的fallback方法。
 * 那现在zuul对api服务集群进行了反向代理，集成了hystrix，那zuul也能fallback了。
 * @author liupeqing
 * @date 2018/10/8 19:24
 */
@Component
@Slf4j
public class AdminFallbackProvider implements FallbackProvider {

    /**
     * 如果请求失败，则返回什么给客户端
     * @param cause
     * @return
     */
    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.SERVICE_UNAVAILABLE;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.SERVICE_UNAVAILABLE.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                if (cause !=null && cause.getMessage() != null){
                    log.error("调用异常",getRoute(),cause.getMessage());
                    return new ByteArrayInputStream(cause.getMessage().getBytes());
                }else {
                    log.error("调用:{} 异常：{}", getRoute(), MessageConstant.BUSINESS_ADMIN_NOTSUPPORT);
                    return new ByteArrayInputStream(
                            MessageConstant.BUSINESS_ADMIN_NOTSUPPORT
                                    .getBytes());
                }
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return httpHeaders;
            }
        };
    }

    /**
     * api服务id，如果需要所有调用都支持回退，则return "*"或return null
     * 本次只对Admin服务进行回退
     * @return
     */
    @Override
    public String getRoute() {
        return ServiceIdConstant.ADMIN_SERVICE;
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return fallbackResponse(null);
    }
}
