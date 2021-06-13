package com.tony.configration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ComsumerConfig {

    /**
     * 使用了@LoadBalanced这个注解以后 ，会在restTemplate里面通过restTemplate.setInterceptors放入LoadBalancerInterceptor
     * 这个过滤器会在 请求远程成接口的时候 动态判断请求的域是不是  负载 负载均衡支付的服务的地址，如果是，那么就会代理 使用 这个负载均衡器 来调用。
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
