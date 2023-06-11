package com.shu.tongzhanbu.component.config;


import com.shu.tongzhanbu.component.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * @author 唐延清
 */
@Service(value = "el")
public class ElPermissionConfig {

    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        List<String> elPermissions = SecurityUtils.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        List<String> asList = Arrays.asList(permissions);
        System.out.println(elPermissions);

        return elPermissions.contains("admin") || elPermissions.stream().anyMatch(s -> {
            boolean b = false;
            try {
                Pattern.compile(s);
                b = asList.stream().anyMatch(str -> str.matches(s));
            } catch (PatternSyntaxException ignore) {
                b = asList.stream().anyMatch(str -> str.equals(s));
            }
            return b;
        });
    }
}
