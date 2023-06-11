package com.shu.tongzhanbu.admindb.repository;



import com.shu.tongzhanbu.admindb.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author tangyanqing
 * Description:
 * Date: 2019-04-22
 * Time: 13:59
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByUrlEqualsAndNameEquals(String url, String roleName);

    List<Permission> findByNameEquals(String roleName);
}
