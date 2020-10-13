package com.imooc;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-24 20:57
 * </pre>
 */
@SpringBootApplication
@MapperScan("com.imooc.mapper")
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
