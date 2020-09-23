package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-25 22:00
 * </pre>
 */

@Configuration
@EnableSwagger2
public class Swagger2Config {
    //localhost:8088/swagger-ui.html
    //localhost:8088/doc.html

    //配置swagger2核心配置 docket
    @Bean
    public Docket createDcoket() {
        return  new Docket(DocumentationType.SWAGGER_2) //指定api类型
                        .apiInfo(this.apiInfo())                      //用于定义api文档汇总信息
                        .select().apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))
                        .paths(PathSelectors.any())
                        .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("老王天天吃")
                .contact(new Contact("wangyujin", "http://wwww.small11.com", "17640113570@163.com"))
                .description("专为老王天天吃提供api文档")
                .version("1.0.1")
                .build();
    }
}
