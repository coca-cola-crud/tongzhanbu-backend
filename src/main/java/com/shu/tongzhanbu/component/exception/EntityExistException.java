package com.shu.tongzhanbu.component.exception;

/**
 * @author 唐延清
 * @date 2018-11-23
 */
public class EntityExistException extends RuntimeException {

    public EntityExistException(Class<?> clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return "记录重复："
                + " " + field + " " + val + " 请检查！";
    }
}