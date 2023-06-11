package com.shu.tongzhanbu.service.dto;

import com.shu.tongzhanbu.component.annotation.Query;

public class EditlogQueryCriteria {
    @Query(order = Query.Order.DESC)
    private Long id;
}
