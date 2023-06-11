package com.shu.tongzhanbu.admindb.entity;


import com.shu.tongzhanbu.component.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author tangyanqing
 * Description:
 * Date: 2019-04-22
 * Time: 13:41
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "sys_permission")
public class Permission extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3862212097990994081L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String url;

    public Permission(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public Permission(String url, String name) {
        this.name = name;
        this.url = url;
    }
}
