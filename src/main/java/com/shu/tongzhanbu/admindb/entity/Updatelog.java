package com.shu.tongzhanbu.admindb.entity;

import com.shu.tongzhanbu.component.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "sys_updatelog")
@NoArgsConstructor
public class Updatelog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name="type",nullable = false)
    private String type;

}
