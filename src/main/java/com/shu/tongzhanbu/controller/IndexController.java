package com.shu.tongzhanbu.controller;


import com.alibaba.fastjson.JSONObject;
import com.shu.tongzhanbu.admindb.entity.Role;
import com.shu.tongzhanbu.admindb.entity.User;
import com.shu.tongzhanbu.component.annotation.Log;
import com.shu.tongzhanbu.component.apipath.ApiRestController;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.service.UserCacheManager;
import com.shu.tongzhanbu.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author tangyanqing
 * Description:
 * Date: 2020-02-26
 * Time: 14:56
 */
@ApiRestController
@RequiredArgsConstructor
@Api(tags = "测试")
public class IndexController {
    private final UserService userService;
    private final UserCacheManager userCacheManager;

    @GetMapping("/logout")
    public ResultBean clearCache(HttpServletResponse response){
        String name;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            name = authentication.getName();

            userCacheManager.cleanUserCache(name);
            System.out.println("清理"+name);
            return ResultBean.success();
//            System.out.println(name);
        } catch (Exception e) {
            response.setStatus(401);
            return null;

        }


    }

    @RequestMapping("/getUserLoginInfo")
    @ResponseBody
//    @PreAuthorize("@el.check()")
//    @PreAuthorize("@el.check('/xxx')")
    public JSONObject getUserLoginInfo(HttpServletResponse response) {
        String name;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            name = authentication.getName();
//            System.out.println(name);
        } catch (Exception e) {
            response.setStatus(401);
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        User user = userService.findByUserName(name);
        if (user == null) {
            return jsonObject;
        }
        jsonObject.put("name", user.getXingming());
        jsonObject.put("userId", user.getUid());
        jsonObject.put("email", user.getEmail());
        jsonObject.put("ptype", user.getPtype());
        jsonObject.put("sex", user.getGender());
        jsonObject.put("title", user.getTitle());
        jsonObject.put("yuanxi", user.getYuanxi());
        jsonObject.put("isAdmin",user.getIsAdmin());
        Set<Role> roles = user.getRoles();
        if (roles.isEmpty()) {
            response.setStatus(401);
            return null;
        }
        Iterator<Role> iterator = roles.iterator();

        List<String> rolesName = new ArrayList<>();
        while (iterator.hasNext()) {
            rolesName.add(iterator.next().getName());
        }
        jsonObject.put("roles", rolesName);
        jsonObject.put("id", user.getId());

        return jsonObject;
    }

}
