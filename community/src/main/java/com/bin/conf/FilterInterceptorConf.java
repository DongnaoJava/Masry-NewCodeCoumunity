package com.bin.conf;

import com.bin.controller.Interceptor.LoginInterceptor;
import com.bin.controller.Interceptor.LoginRequiredInterceptor;
import com.bin.controller.filter.EncodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServlet;
import java.util.Collections;

@Configuration
public class FilterInterceptorConf implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
                .addPathPatterns("/**");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
    }

    @Bean
    public FilterRegistrationBean<EncodeFilter> getFilter(){
        FilterRegistrationBean<EncodeFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        EncodeFilter encodeFilter = new EncodeFilter();
        filterFilterRegistrationBean.setFilter(encodeFilter);
        filterFilterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        return filterFilterRegistrationBean;
    }
}
