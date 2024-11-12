package com.nhnacademy.mini_dooray.ssacthree_front.config;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.filter.ReissueFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthAdapter reissueFilter;

    @Bean
    public FilterRegistrationBean<ReissueFilter> filterRegistrationBean() {
        FilterRegistrationBean<ReissueFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ReissueFilter(reissueFilter));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
