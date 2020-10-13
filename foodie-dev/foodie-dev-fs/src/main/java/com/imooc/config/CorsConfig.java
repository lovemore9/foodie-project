package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-02-26 22:05
 * </pre>
 */
@Configuration
public class CorsConfig {

    public CorsConfig() {};

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");

        //设置是否发送cookie
        config.setAllowCredentials(true);

        config.addAllowedMethod("*");

        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", config);

        return new CorsFilter(source);
    }
}
