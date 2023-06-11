
package com.shu.tongzhanbu.service.mapstruct;

import com.shu.tongzhanbu.admindb.entity.Role;
import com.shu.tongzhanbu.component.base.BaseMapper;
import com.shu.tongzhanbu.service.dto.RoleSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 唐延清
 * @date 2019-5-23
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleSmallMapper extends BaseMapper<RoleSmallDto, Role> {

}
