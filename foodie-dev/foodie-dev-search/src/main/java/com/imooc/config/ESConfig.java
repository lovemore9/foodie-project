package com.imooc.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-29 11:29
 * </pre>
 */

@Configuration
public class ESConfig {

    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
