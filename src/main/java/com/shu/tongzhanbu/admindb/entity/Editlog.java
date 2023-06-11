package com.shu.tongzhanbu.admindb.entity;

import com.shu.tongzhanbu.component.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "sys_editlog")
public class Editlog extends BaseEntity {
    @Id
    @Column(name="editlog_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String xingming;
    private String content;//修改的内容
    private String editperson;//被修改者


}
