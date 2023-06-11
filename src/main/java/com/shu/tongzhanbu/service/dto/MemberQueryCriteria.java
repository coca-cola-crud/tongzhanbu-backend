package com.shu.tongzhanbu.service.dto;

import com.shu.tongzhanbu.component.annotation.Query;
import io.swagger.models.auth.In;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class MemberQueryCriteria {
    @Query
    private String xingming;
    @Query
    private String gonghao;

    @Query(order = Query.Order.DESC)
    private Long id;


    @Query
    private Integer isdelete;

}
