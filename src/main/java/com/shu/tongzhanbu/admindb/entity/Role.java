package com.shu.tongzhanbu.admindb.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shu.tongzhanbu.component.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true, value = {"users"})
@Table(name = "sys_role")
public class Role extends BaseEntity implements GrantedAuthority {

    private static final long serialVersionUID = 2389685713973500716L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
