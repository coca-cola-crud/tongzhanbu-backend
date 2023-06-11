package com.shu.tongzhanbu.component.apipath;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author tangyanqing
 * Description:
 * Date: 2020-11-27
 * Time: 9:55
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    @Resource
    private ApiPathProperties apiPathProperties;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .addPathPrefix(apiPathProperties.getGlobalPrefix(), c -> c.isAnnotationPresent(ApiRestController.class));
    }

}