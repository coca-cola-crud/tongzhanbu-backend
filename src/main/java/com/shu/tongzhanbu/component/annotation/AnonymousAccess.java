package com.shu.tongzhanbu.component.annotation;

import java.lang.annotation.*;

/**
 * @author 唐延清
 * 用于标记匿名访问方法
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {

}
