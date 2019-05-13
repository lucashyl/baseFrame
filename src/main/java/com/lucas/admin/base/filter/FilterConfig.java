package com.lucas.admin.base.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * Created by hanyonglu on 2018/6/20.
 */
@Configuration
public class FilterConfig {
    /**
     * 配置过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(loginFilter());
        registration.addUrlPatterns("/front/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setOrder(2);
        registration.setName("loginFilter");
        return registration;
    }

    /**
     * 创建一个bean
     * @return
     */
    @Bean(name = "loginFilter")
    public Filter loginFilter() {
        return new LoginFilter();
    }
}


