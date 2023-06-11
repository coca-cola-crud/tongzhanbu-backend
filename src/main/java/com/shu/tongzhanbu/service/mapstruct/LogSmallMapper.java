
package com.shu.tongzhanbu.service.mapstruct;


import com.shu.tongzhanbu.admindb.entity.Log;
import com.shu.tongzhanbu.component.base.BaseMapper;
import com.shu.tongzhanbu.service.dto.LogSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 唐延清
 * @date 2019-5-22
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogSmallMapper extends BaseMapper<LogSmallDTO, Log> {

}