package com.bin.conf;

import com.bin.filter.EncodeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class FilterConf {
    @Bean
    public FilterRegistrationBean<EncodeFilter> getFilter(){
        FilterRegistrationBean<EncodeFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        EncodeFilter encodeFilter = new EncodeFilter();
        filterFilterRegistrationBean.setFilter(encodeFilter);
        filterFilterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        return filterFilterRegistrationBean;
    }
}
