package com.shu.tongzhanbu.component.init;


import com.shu.tongzhanbu.admindb.entity.Permission;
import com.shu.tongzhanbu.admindb.entity.Role;
import com.shu.tongzhanbu.admindb.repository.PermissionRepository;
import com.shu.tongzhanbu.admindb.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.shu.tongzhanbu.component.common.Const.*;


/**
 * @author tangyanqing
 * Description:
 * Date: 2020-09-08
 * Time: 12:53
 */
@Component
@RequiredArgsConstructor
public class DataInitiator implements ApplicationRunner {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;


    @Value("${api.path.global-prefix}")
    private String apiGlobalPrefix;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //init system roles
        Role roleAdmin = roleRepository.findRoleByName(SYSTEM_ROLE_ADMIN);
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setName(SYSTEM_ROLE_ADMIN);
            roleAdmin = roleRepository.save(roleAdmin);
        }
        Role roleUser = roleRepository.findRoleByName(SYSTEM_ROLE_USER);
        if (roleUser == null) {
            roleUser = new Role();
            roleUser.setName(SYSTEM_ROLE_USER);
            roleUser = roleRepository.save(roleUser);
        }
        Role roleAnonymous = roleRepository.findRoleByName(SYSTEM_ROLE_ANONYMOUS);
        if (roleAnonymous == null) {
            roleAnonymous = new Role();
            roleAnonymous.setName(SYSTEM_ROLE_ANONYMOUS);
            roleAnonymous = roleRepository.save(roleAnonymous);
        }
        Role roleDep = roleRepository.findRoleByName(SYSTEM_ROLE_DEP);
        if (roleDep == null) {
            roleDep = new Role();
            roleDep.setName(SYSTEM_ROLE_DEP);
            roleDep = roleRepository.save(roleDep);
        }

        // init permission

        //admin
        Permission adminPermission = permissionRepository.findByUrlEqualsAndNameEquals(SYSTEM_PERMISSION_ALL, SYSTEM_ROLE_ADMIN);
        if (adminPermission == null) {
            adminPermission = new Permission(SYSTEM_PERMISSION_ALL, SYSTEM_ROLE_ADMIN);
            adminPermission = permissionRepository.save(adminPermission);
        }

        //anonymous
        Permission anonymousPermission1 = permissionRepository.findByUrlEqualsAndNameEquals(apiGlobalPrefix + SYSTEM_PERMISSION_PERMIT_ANONYMOUS1, SYSTEM_ROLE_ANONYMOUS);
        if (anonymousPermission1 == null) {
            anonymousPermission1 = new Permission(apiGlobalPrefix + SYSTEM_PERMISSION_PERMIT_ANONYMOUS1, SYSTEM_ROLE_ANONYMOUS);
            anonymousPermission1 = permissionRepository.save(anonymousPermission1);
        }
        Permission anonymousPermission2 = permissionRepository.findByUrlEqualsAndNameEquals(apiGlobalPrefix + SYSTEM_PERMISSION_PERMIT_ANONYMOUS2, SYSTEM_ROLE_ANONYMOUS);
        if (anonymousPermission2 == null) {
            anonymousPermission2 = new Permission(apiGlobalPrefix + SYSTEM_PERMISSION_PERMIT_ANONYMOUS2, SYSTEM_ROLE_ANONYMOUS);
            anonymousPermission2 = permissionRepository.save(anonymousPermission2);
        }

        //user
        Permission userPermission1 = permissionRepository.findByUrlEqualsAndNameEquals(apiGlobalPrefix + SYSTEM_PERMISSION_USER1, SYSTEM_ROLE_USER);
        if (userPermission1 == null) {
            userPermission1 = new Permission(apiGlobalPrefix + SYSTEM_PERMISSION_USER1, SYSTEM_ROLE_USER);
            userPermission1 = permissionRepository.save(userPermission1);
        }

        Permission userPermission2 = permissionRepository.findByUrlEqualsAndNameEquals(apiGlobalPrefix + SYSTEM_PERMISSION_USER2, SYSTEM_ROLE_USER);
        if (userPermission2 == null) {
            userPermission2 = new Permission(apiGlobalPrefix + SYSTEM_PERMISSION_USER2, SYSTEM_ROLE_USER);
            userPermission2 = permissionRepository.save(userPermission2);
        }
    }
}
