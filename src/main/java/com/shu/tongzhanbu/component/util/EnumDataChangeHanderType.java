package com.shu.tongzhanbu.component.util;



import lombok.Getter;

/**
 * title EnumDataChangeHanderType
 * Description 属性变更类型枚举
 * CreateDate 2022/5/31 23:06
 *
 * @author izhouy
 */
@Getter
public enum EnumDataChangeHanderType {
    // 属性变更类型枚举
    DELETE("DEL", "删除"),
    UPDATE("UPD", "修改"),
    SAVE("SAVE", "新增");
//    ADD("ADD", "新增");

    private String code;
    private String name;

    EnumDataChangeHanderType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
