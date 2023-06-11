package com.shu.tongzhanbu.component.exception;

/**
 * @author 唐延清
 * @date 2018-11-23
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, String field, String val) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return "记录不存在："
                + " " + field + " " + val + " 请检查！";
    }
}