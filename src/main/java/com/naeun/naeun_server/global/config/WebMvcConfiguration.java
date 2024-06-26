package com.naeun.naeun_server.global.config;

import com.naeun.naeun_server.global.common.filter.JwtAuthorizationFilter;
import com.naeun.naeun_server.global.util.JwtUtil;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final JwtUtil jwtUtil;

    @Bean
    public FilterRegistrationBean<Filter> filterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(new JwtAuthorizationFilter(jwtUtil));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
