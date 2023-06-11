package com.shu.tongzhanbu.component.util;

import lombok.Data;

@Data
public class ComparbleResult {

    /**
     * 变更字段
     */
    private String fieldName;

    /**
     * 变更前类的内容容
     */
    private Object fieldContent;
    /**
     * 变更后类的内容容
     */
    private Object newFieldContent;
    /**
     * 变更的枚举类型
     */
    private String handerType;

}
