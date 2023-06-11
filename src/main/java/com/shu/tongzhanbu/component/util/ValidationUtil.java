package com.shu.tongzhanbu.component.util;


import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import com.shu.tongzhanbu.component.exception.BadRequestException;

/**
 * 验证工具
 *
 * @author 唐延清
 * @date 2018-11-23
 */
public class ValidationUtil {

    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter, Object value) {
        if (ObjectUtil.isNull(obj)) {
            String msg = entity + " 不存在: " + parameter + " is " + value;
            throw new BadRequestException(msg);
        }
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String email) {
        return Validator.isEmail(email);
    }
}
