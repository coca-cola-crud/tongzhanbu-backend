package com.shu.tongzhanbu.component.apipath;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tangyanqing
 * Description:
 * Date: 2020-11-27
 * Time: 9:55
 */
@Component
@ConfigurationProperties(prefix = "api.path")
@Data
public class ApiPathProperties {

    String globalPrefix = "api";
}
