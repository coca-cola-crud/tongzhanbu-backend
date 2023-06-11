package com.shu.tongzhanbu.admindb.repository;


import com.shu.tongzhanbu.admindb.entity.Role;
import com.shu.tongzhanbu.admindb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author tangyanqing
 * Description:
 * Date: 2019-03-29
 * Time: 10:05
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return
     */
    User findUserByUid(String userName);

    /**
     * 根据用户邮箱查询用户
     *
     * @param email
     * @return
     */
    User findUserByEmail(String email);


    List<User> findAllByRolesContains(Role role);

    @Query(value = "select * from sys_user as a left join  sys_user_sys_role as b on b.user_id = a.id where user_id is not null",nativeQuery = true)
    List<User> findAdmin();
}
