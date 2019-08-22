package com.neuedu;

import com.google.common.collect.Lists;
import com.neuedu.interceptors.PortalAuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class MySpringBootConfig implements WebMvcConfigurer{
    @Autowired
    PortalAuthorityInterceptor portalAuthorityInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> addPatterns= Lists.newArrayList();
        addPatterns.add("/user/**");
        addPatterns.add("/cart/**");
        addPatterns.add("/order/**");
        addPatterns.add("/shipping/**");
        List<String> excludePatterns=Lists.newArrayList();
        excludePatterns.add("/user/register.do");
        excludePatterns.add("/user/login.do/**");
        excludePatterns.add("/user/forget_get_question.do");
        excludePatterns.add("/user/forget_check_answer.do");
        excludePatterns.add("/user/forget_reset_password.do");
        excludePatterns.add("/user/check_valid.do");
        excludePatterns.add("/order/alipay_callback.do");

        registry.addInterceptor(portalAuthorityInterceptor).addPathPatterns(addPatterns)
        .excludePathPatterns(excludePatterns);


    }
}
