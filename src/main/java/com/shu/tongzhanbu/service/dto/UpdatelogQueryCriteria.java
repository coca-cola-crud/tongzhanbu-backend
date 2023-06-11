package com.shu.tongzhanbu.service.dto;

import com.shu.tongzhanbu.component.annotation.Query;
import lombok.Data;

@Data
public class UpdatelogQueryCriteria {
    @Query
    private String type;
}
