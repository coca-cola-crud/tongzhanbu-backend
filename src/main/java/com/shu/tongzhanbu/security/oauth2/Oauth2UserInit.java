package com.shu.tongzhanbu.security.oauth2;


import com.shu.tongzhanbu.admindb.entity.Member;
import com.shu.tongzhanbu.admindb.entity.Role;
import com.shu.tongzhanbu.admindb.entity.User;
import com.shu.tongzhanbu.component.common.CommonBeans;
import com.shu.tongzhanbu.component.common.Const;
import com.shu.tongzhanbu.service.MemberService;
import com.shu.tongzhanbu.service.RoleService;
import com.shu.tongzhanbu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author tangyanqing
 * Description:
 * Date: 2022-02-22
 * Time: 17:21
 */
@Component
@RequiredArgsConstructor
public class Oauth2UserInit {

    private final UserService userService;
    private final MemberService memberService;

    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void initUser(Map<String, Object> map) {
        String userid = (String) map.get("userid");
        String username = (String) map.get("username");
        String email = (String) map.get("email");
        String ptype = (String) map.get("ptype");
        String sex = (String) map.get("sex");
        String title = (String) map.get("title");
        String yuanxi = (String) map.get("yuanxi");
        User user = userService.findByUserName(userid);
        if (user == null) {
            user = new User();
        }
        user.setUid(userid);
        user.setXingming(username);
        user.setEmail(email);
        user.setPtype(ptype);
        user.setGender(sex);
        user.setTitle(title);
        user.setYuanxi(yuanxi);
        user.setEnabled(true);
        user.setHashedPassword(bCryptPasswordEncoder.encode(user.getUid() + CommonBeans.PRIVATE));
        Role roleUser = roleService.getOneByName(Const.SYSTEM_ROLE_USER);
        Role roleAdmin = roleService.getOneByName(Const.SYSTEM_ROLE_ADMIN);
        if (Arrays.asList("L0061180", "L0061181","21721641").contains(userid)) {
            user.setIsAdmin(true);
            Set<Role> roles = user.getRoles();
//            roles.add(roleUser);
            roles.add(roleAdmin);
            user.setRoles(roles);
        }
//        Member member = new Member();
//        member.setGonghao(userid);
//        member.setXingming(username);
//
//        memberService.save(member);
        userService.save(user);
    }

}
