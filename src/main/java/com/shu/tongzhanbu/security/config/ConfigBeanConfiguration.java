
package com.shu.tongzhanbu.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @apiNote 配置文件转换Pojo类的 统一配置 类
 * @author: liaojinlong
 * @date: 2020/6/10 19:04
 */
@Configuration
public class ConfigBeanConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "jwt")
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }
}
