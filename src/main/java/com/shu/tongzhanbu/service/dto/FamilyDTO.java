package com.shu.tongzhanbu.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FamilyDTO {

    @NotBlank(message = "关系不能为空")
    private String guanxi;//关系
    @NotBlank(message = "姓名不能为空")
    private String name;//姓名
    @NotBlank(message = "身份证不能为空")
    private String card;//身份证
    @NotBlank(message = "政治面貌不能为空")
    private String zzmianmao;//政治面貌

    private String yjcjjuliu;//永久长久居留
    private String jobdanwei;//工作单位
}
