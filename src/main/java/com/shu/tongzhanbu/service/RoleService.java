package com.shu.tongzhanbu.service;


import com.shu.tongzhanbu.admindb.entity.Permission;
import com.shu.tongzhanbu.admindb.entity.Role;
import com.shu.tongzhanbu.admindb.repository.PermissionRepository;
import com.shu.tongzhanbu.admindb.repository.RoleRepository;
import com.shu.tongzhanbu.admindb.repository.UserRepository;
import com.shu.tongzhanbu.component.util.StringUtils;
import com.shu.tongzhanbu.service.dto.AuthorityDto;
import com.shu.tongzhanbu.service.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author tangyanqing
 * Description:
 * Date: 2019-03-29
 * Time: 14:51
 */
@Service("roleService")
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public Role getOneByName(String roleDefault) {
        return roleRepository.findRoleByName(roleDefault);
    }

    public List<AuthorityDto> mapToGrantedAuthorities(UserLoginDto user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
//        System.out.println(user.getIsAdmin());
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(AuthorityDto::new)
                    .collect(Collectors.toList());
        }
        Set<Role> roles = roleRepository.findByUserId(user.getId());
        permissions = roles.stream().map(role -> permissionRepository.findByNameEquals(role.getName()).stream().map(Permission::getUrl).collect(Collectors.toSet()))
                .flatMap((Function<Set<String>, Stream<String>>) Collection::stream).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        return permissions.stream().map(AuthorityDto::new)
                .collect(Collectors.toList());
    }
}
