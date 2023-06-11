package com.shu.tongzhanbu.admindb.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.shu.tongzhanbu.component.base.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "sys_member")
public class Member extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 6329638893112038331L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "工号不能为空")
    private String gonghao;
    @NotBlank(message = "姓名不能为空")
    private String xingming;

    private String picture;//照片

    private String yuangongzu;//员工组，平台
    private String yuangongzizu;//员工子组，平台
    private String country;//国籍，平台

    
    private String gender;//平台
    private String zongjiao;//手动
    private String minzu;//平台
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String birthday;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String joinworkyearmonth;
    private String jiguan;
    private String birthplace;
    private String health;

    private String  speciality;//特长
    private String jishuzhicheng;//技术职称
    private String zhichengjibie;//职称级别
    private String zhuanjiatype;//专家类型
    private String zhiwulevel;//职务级别
    private String renxuantype;//人选类别

    private String card;
    private String danwei;//单位高校
    private String dangpai;//党派
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private String joindptime;//参加党派时间
    private String seconddangpai;//第二党派
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private String joinsdptime;//参加第二党派
    private String tongzhanparty;//统战团体

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_members_partys",
            joinColumns = {@JoinColumn(name = "id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "party_id",referencedColumnName = "party_id")})
    private Set<Tongzhanparty> tzpartys;


    private String abroad;//海外学习经历 如果有则是填充国家
    private String xuewei;//学位
    private String college;//毕业学校&专业

    private String zzxuewei;//学位
    private String zzcollege;//毕业学校&专业
    private String rendaInfo;//人大担任情况
    private String zhengxieInfo;//政协委员情况
    private String yjjuliuquan;//国（境）永久居留权
    private String yjjuliuxuke;//国（境）永久居留许可
    private String jobInfo;//工作单位及职务
    private String nowsocialjob;//现任社会职务
    private String jobaddress;//工作地址
    private String jobphone;//工作邮编
    private String jobyoubian;//工作邮编
    private String hujiaddress;//户籍地址
    private String hujiphone;//户籍电话
    private String hujiyoubian;//户籍邮编
    private String juzhuaddress;//居住地址
    private String juzhuphone;//居住电话
    private String juzhuyoubian;//居住邮编
    private String telephone;//手机号码
    private String email;//邮箱地址
    private String jianli;//简历
    private String chengjiu;//成就
    private String huojiang;//获奖
    private Integer age;//年龄
    private String agelabel;//年领标签
    private String dep1;// 单位的一级部门
    private String dep2; //单位的二级部门



    @Column(columnDefinition="int default 0",nullable=false)
    private Integer isdelete;


}
