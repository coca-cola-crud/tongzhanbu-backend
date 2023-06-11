
package com.shu.tongzhanbu.service.dto;

import com.shu.tongzhanbu.component.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * @author 唐延清
 * @date 2018-11-23
 */
@Getter
@Setter
public class UserDto extends BaseDTO implements Serializable {

    private Long id;

    private Set<RoleSmallDto> roles;

    private String uid;

    private String xingming;

    private String yuanxi;

    private String hashedPassword;

    private String email;

    private String ptype;
    private String gender;
    private String title;
    private Boolean enabled = false;
    private Boolean isAdmin ;
}
