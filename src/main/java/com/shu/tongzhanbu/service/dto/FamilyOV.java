package com.shu.tongzhanbu.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class FamilyOV {
    private Long id;

    private String guanxi;//关系

    private String name;//姓名

    private String card;//身份证

    private String zzmianmao;//政治面貌

    private String yjcjjuliu;//永久长久居留
    private String jobdanwei;//工作单位
    private Boolean isEdit;
    private Boolean isAdd;
}
