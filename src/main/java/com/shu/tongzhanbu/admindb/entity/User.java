package com.shu.tongzhanbu.admindb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shu.tongzhanbu.component.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true, value = {"roles"})
@Table(name = "sys_user")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6329638893112038331L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String uid;

    private String xingming;

    private String yuanxi;

    private String hashedPassword;

    private String email;

    private String ptype;
    private String gender;
    private String title;
    private Boolean enabled = false;
    private Boolean isAdmin = false;
    private Boolean isEdit = false;
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


}
