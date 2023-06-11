package com.shu.tongzhanbu.admindb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "sys_tongzhanparty")
public class Tongzhanparty {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id")
    private Long id;

    @Column(name = "party_name")
    private String name;

}
