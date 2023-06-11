package com.shu.tongzhanbu.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shu.tongzhanbu.admindb.entity.Member;
import com.shu.tongzhanbu.admindb.entity.Role;
import com.shu.tongzhanbu.admindb.entity.User;
import com.shu.tongzhanbu.admindb.repository.MemberRepository;
import com.shu.tongzhanbu.admindb.repository.RoleRepository;
import com.shu.tongzhanbu.admindb.repository.UserRepository;
import com.shu.tongzhanbu.component.common.Const;
import com.shu.tongzhanbu.component.exception.EntityNotFoundException;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.component.util.TongbuUtil;
import com.shu.tongzhanbu.service.dto.AdminDTO;
import com.shu.tongzhanbu.service.dto.UserDto;
import com.shu.tongzhanbu.service.dto.UserLoginDto;
import com.shu.tongzhanbu.service.mapstruct.UserLoginMapper;
import com.shu.tongzhanbu.service.mapstruct.UserMapper;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tangyanqing
 * Description:
 * Date: 2019-03-29
 * Time: 10:07
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;
//    private final UserCacheManager userCacheManager;

    private final MemberRepository memberRepository;


    public User findByUserName(String userName) {
        return userRepository.findUserByUid(userName);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<UserDto> findAllAdmin() {
//        Role roleAdmin = roleRepository.findRoleByName(Const.SYSTEM_ROLE_ADMIN);
//        return userRepository.findAllByRolesContains(roleAdmin);


        return userMapper.toDto(userRepository.findAdmin());
    }


    public List<JSONObject> userList() {
//        Role roleUser = roleRepository.findRoleByName("ROLE_user");
        List<User> all = userRepository.findAll();
        List<JSONObject> list = new ArrayList<>();
        for (User user : all) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", user.getUid());
            jsonObject.put("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
            jsonObject.put("name", user.getXingming());
            list.add(jsonObject);
        }
        return list;
    }

    public boolean isDepUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name == null) {
            return false;
        }
        Role roleDep = roleRepository.findRoleByName(Const.SYSTEM_ROLE_DEP);
        User user = findByUserName(name);
        // 只有一个角色且是部门管理角色，则返回true
        return user.getRoles().size() == 1 && user.getRoles().contains(roleDep);
    }

    public boolean hasDepRole() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name == null) {
            return false;
        }
        Role roleDep = roleRepository.findRoleByName(Const.SYSTEM_ROLE_DEP);
        User user = findByUserName(name);
        // 有部门管理角色，则返回true
        return user.getRoles().contains(roleDep);
    }

    public boolean isAdminUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name == null) {
            return false;
        }
        Role roleAdmin = roleRepository.findRoleByName(Const.SYSTEM_ROLE_ADMIN);
        User user = findByUserName(name);
        return user.getRoles().contains(roleAdmin);
    }

    public boolean isCommonUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name == null) {
            return false;
        }
        Role roleAdmin = roleRepository.findRoleByName(Const.SYSTEM_ROLE_ADMIN);
        Role roleUser = roleRepository.findRoleByName(Const.SYSTEM_ROLE_USER);
        User user = findByUserName(name);
        // 包括user 角色，但不包括admin角色
        return user.getRoles().contains(roleUser) && !user.getRoles().contains(roleAdmin);
    }

    public User currentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByUserName(name);
    }


    public void deleteUserRole(String uid, String role) {
        roleRepository.deleteUserRole(uid, role);
    }

    private final UserLoginMapper userLoginMapper;

    public UserLoginDto getLoginData(String userName) {
        User user = userRepository.findUserByUid(userName);
        if (user == null) {
            throw new EntityNotFoundException(User.class, "用户名", userName);
        } else {
            return userLoginMapper.toDto(user);
        }
    }

//    public ResultBean setAdmin(String uid) {
//        User user = userRepository.findUserByUid(uid);
//        user.setIsAdmin();
//
//    }

    public ResultBean NomarlUser(String uid) {
        User user = userRepository.findUserByUid(uid);
        user.setIsAdmin(false);
        userRepository.save(user);
        deleteUserRole(uid,"ROLE_admin");
        deleteUserRole(uid,"ROLE_user");
        return ResultBean.success("移除用户成功");
    }

    public ResultBean addAdmin(AdminDTO adminDTO) {
        User user1 = userRepository.findUserByUid(adminDTO.getUid());
        if(user1!=null){//用户已登录过该系统
            user1.setIsAdmin(true);
            Role roleAdmin = roleService.getOneByName(Const.SYSTEM_ROLE_ADMIN);
            Set<Role> roles = user1.getRoles();
            if(roles.size()>0){
                return ResultBean.warn("该用户已有角色");
            }
            roles.add(roleAdmin);
            user1.setRoles(roles);
            userRepository.save(user1);
            return ResultBean.success("设置管理员成功");
        }else{
            User user = new User();
            JSONObject jsonObject = TongbuUtil.getName(adminDTO.getUid(),"");
            if(jsonObject == null){
                return ResultBean.error("没有该用户");
            }else {
                User user2 = new User();
                user2.setUid(jsonObject.getJSONObject("data").getString("uid"));
                user2.setXingming(jsonObject.getJSONObject("data").getString("name"));
                user2.setYuanxi(jsonObject.getJSONObject("data").getString("dept"));
                user2.setPtype(jsonObject.getJSONObject("data").getString("type"));
//                System.out.println(jsonObject);
                user2.setIsAdmin(true);
                Role roleAdmin = roleService.getOneByName(Const.SYSTEM_ROLE_ADMIN);
                Set<Role> roles = user2.getRoles();
                roles.add(roleAdmin);
                user2.setRoles(roles);
                userRepository.save(user2);

                return ResultBean.success("设置管理员成功");
            }

//            Member member = memberRepository.findMemberByGonghao(adminDTO.getUid());
//            if(member!=null){
//              adminDTO.setXingming(member.getXingming());
//            }else {
//                return ResultBean.warn("没有该用户");
//            }


        }


    }

    public ResultBean getAllmembersEasyInfo() {

         List<Member> members = memberRepository.findAllMembers();
        JSONArray jsonArray = new JSONArray();
        for(Member member:members){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid",member.getGonghao());
            jsonObject.put("value",member.getXingming());
            jsonArray.add(jsonObject);
        }
        return ResultBean.success(jsonArray);


    }

    public ResultBean addNormalUser(AdminDTO adminDTO) {
        User user1 = userRepository.findUserByUid(adminDTO.getUid());
        if(user1!=null){//用户已登录过该系统
            user1.setIsAdmin(true);
            Role roleUser = roleService.getOneByName(Const.SYSTEM_ROLE_USER);
            Set<Role> roles = user1.getRoles();
            if(roles.size()>0){
                return ResultBean.warn("该用户已有角色");
            }
            roles.add(roleUser);
            user1.setRoles(roles);
            userRepository.save(user1);
            return ResultBean.success("设置普通用户成功");
        }else{
            User user = new User();
            JSONObject jsonObject = TongbuUtil.getName(adminDTO.getUid(),"");
            if(jsonObject == null){
                return ResultBean.error("没有该用户");
            }else {
                User user2 = new User();
                user2.setUid(jsonObject.getJSONObject("data").getString("uid"));
                user2.setXingming(jsonObject.getJSONObject("data").getString("name"));
                user2.setYuanxi(jsonObject.getJSONObject("data").getString("dept"));
                user2.setPtype(jsonObject.getJSONObject("data").getString("type"));
//                System.out.println(jsonObject);
                user2.setIsAdmin(true);
                Role roleUser = roleService.getOneByName(Const.SYSTEM_ROLE_USER);
                Set<Role> roles = user2.getRoles();
                roles.add(roleUser);
                user2.setRoles(roles);
                userRepository.save(user2);
                return ResultBean.success("设置普通用户成功");
            }

//            Member member = memberRepository.findMemberByGonghao(adminDTO.getUid());
//            if(member!=null){
//              adminDTO.setXingming(member.getXingming());
//            }else {
//                return ResultBean.warn("没有该用户");
//            }


        }
    }

    public ResultBean editRole(String uid) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(name.equals(uid)){
            return ResultBean.error("不能修改自己的角色");
        }
        User user = userRepository.findUserByUid(uid);
        Set<Role> roles = user.getRoles();
        for(Role role:roles){
            if(role.getName().equals(Const.SYSTEM_ROLE_ADMIN)){
                deleteUserRole(uid,Const.SYSTEM_ROLE_ADMIN);
                Role roleUser = roleService.getOneByName(Const.SYSTEM_ROLE_USER);
                Set<Role> newrole = new HashSet<>();
                newrole.add(roleUser);
                user.setIsAdmin(false);
                user.setRoles(newrole);
            }else {
                deleteUserRole(uid,Const.SYSTEM_ROLE_USER);
                Role roleUser = roleService.getOneByName(Const.SYSTEM_ROLE_ADMIN);
                Set<Role> newrole = new HashSet<>();
                newrole.add(roleUser);
                user.setIsAdmin(true);
                user.setRoles(newrole);
            }

        }
//        UserCacheManager.cleanUserCache(uid);
        userRepository.save(user);
        return ResultBean.success();
    }
}
