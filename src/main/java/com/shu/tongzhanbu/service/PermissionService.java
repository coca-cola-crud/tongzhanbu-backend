package com.shu.tongzhanbu.service;


import com.shu.tongzhanbu.admindb.entity.Permission;
import com.shu.tongzhanbu.admindb.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangyanqing
 * Description:
 * Date: 2019-05-28
 * Time: 12:25
 */
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }
}
