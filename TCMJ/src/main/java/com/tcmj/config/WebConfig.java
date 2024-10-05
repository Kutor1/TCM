package com.tcmj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 重写WebMvcConfigurer接口的addCorsMappings方法，用于配置跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加跨域映射
        registry.addMapping("/**") // 允许所有路径
                .allowedOrigins("http://localhost:5173") // 允许的本地前端地址
                .allowedOrigins("http://43.138.146.197:5173") // 允许的远程前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许的请求头
                .exposedHeaders("Content-Disposition") // 允许前端访问的响应头
                .allowCredentials(true); // 允许携带凭证（如Cookies）
    }

}
