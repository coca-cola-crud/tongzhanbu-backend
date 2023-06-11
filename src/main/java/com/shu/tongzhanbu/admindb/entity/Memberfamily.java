package com.shu.tongzhanbu.admindb.entity;


import com.shu.tongzhanbu.component.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "sys_member_family")
public class Memberfamily extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6329638893112038331L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gonghao;
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
