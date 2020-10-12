package com.imooc.config;

import com.imooc.filter.RequestBodyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.Registration;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-23 14:11
 * </pre>
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean requestBodyFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new RequestBodyFilter());
        registration.setName("requestBodyFilter");
        registration.addUrlPatterns("/*");

        return registration;
    }
}
