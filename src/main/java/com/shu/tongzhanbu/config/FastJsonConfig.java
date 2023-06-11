package com.shu.tongzhanbu.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.context.annotation.Configuration;
/**
 *@Description 配置FastJson
 *@Author
 *@Date 2020/11/9
 *@Version 1.0
 */
@Configuration
public class FastJsonConfig {
    static {
        //全局配置关闭 Fastjson 循环引用
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }
}

