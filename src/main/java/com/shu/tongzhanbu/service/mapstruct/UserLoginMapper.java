
package com.shu.tongzhanbu.service.mapstruct;


import com.shu.tongzhanbu.admindb.entity.User;
import com.shu.tongzhanbu.component.base.BaseMapper;
import com.shu.tongzhanbu.service.dto.UserLoginDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 唐延清
 * @date 2018-11-23
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserLoginMapper extends BaseMapper<UserLoginDto, User> {
}
