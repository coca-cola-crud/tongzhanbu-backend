package com.shu.tongzhanbu.component.util;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangyanqing
 * Description:
 * Date: 2020-08-31
 * Time: 14:46
 */
public class MessageConverter extends MappingJackson2HttpMessageConverter {
    public MessageConverter() {
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_HTML);
        setSupportedMediaTypes(mediaTypes);
    }
}
