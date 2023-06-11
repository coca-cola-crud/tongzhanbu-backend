package com.shu.tongzhanbu.admindb.repository;


import com.shu.tongzhanbu.admindb.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Set;

/**
 * @author tangyanqing
 * Description:
 * Date: 2019-03-29
 * Time: 14:47
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Role findRoleByName(String name);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from sys_user_sys_role where user_id = (select id from sys_user where uid = ?1) and role_id = (select id from sys_role where name = ?2) ")
    void deleteUserRole(String uid, String role);

    @Query(value = "SELECT r.* FROM sys_role r, sys_user_sys_role u WHERE " +
            "r.id = u.role_id AND u.user_id = ?1", nativeQuery = true)
    Set<Role> findByUserId(Long id);
}
