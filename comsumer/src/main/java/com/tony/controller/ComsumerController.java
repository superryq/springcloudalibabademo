package com.tony.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@Slf4j
public class ComsumerController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    /**
     * 不会自动装配，需要自行注入此Bean
     */
    private RestTemplate restTemplate;

    @GetMapping("getInstances")
    public List<ServiceInstance> getInstances() {
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
        HashMap map = new HashMap();

        return list;
    }

    @GetMapping("index")
    public String index() {
        // 需要去访问provider那边的接口
        // 一、发现服务，找到provider实例
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
        log.info("---------------- [{}]",list.size());
        int index = ThreadLocalRandom.current().nextInt(list.size());
        ServiceInstance serviceInstance = list.get(index);
        log.info("[{}]",serviceInstance.toString());
        String url = serviceInstance.getUri() + "/index";
        // 调用相应的接口不能和ribbon共存
        return serviceInstance.getUri() + " - " + this.restTemplate.getForObject(url, int.class);
    }

    @GetMapping("index2")
    public int index2() {
        // 使用ribbon来调用接口，需要在注入RestTemplate时加入@LoadBalance注解
        // ribbon不是spring cloud alibaba的组件,由NetFlix提供
        // ribbon默认使用轮训算法
        return this.restTemplate.getForObject("http://provider/index", int.class);
    }
}
