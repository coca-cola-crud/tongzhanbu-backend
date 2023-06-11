
package com.shu.tongzhanbu.service;

import com.shu.tongzhanbu.component.exception.BadRequestException;
import com.shu.tongzhanbu.component.exception.EntityNotFoundException;
import com.shu.tongzhanbu.service.dto.JwtUserDto;
import com.shu.tongzhanbu.service.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author 唐延清
 * @description 用户缓存管理
 * @date 2022-05-26
 **/
@Component
@RequiredArgsConstructor
public class UserCacheManager {

    private final UserService userService;
    private final RoleService roleService;

    /**
     * 返回用户缓存
     *
     * @return JwtUserDto
     */
    @Cacheable(cacheNames = "user-", key = "#username")
    public JwtUserDto getUserCache(String username) {
        JwtUserDto jwtUserDto;
        UserLoginDto user;
        try {
            user = userService.getLoginData(username);
        } catch (EntityNotFoundException e) {
            // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
            throw new UsernameNotFoundException(username, e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("");
        } else {
            if (!user.getEnabled()) {
                throw new BadRequestException("账号未激活！");
            }
            jwtUserDto = new JwtUserDto(
                    user,
                    roleService.mapToGrantedAuthorities(user)
            );
        }

        return jwtUserDto;
    }


    /**
     * 清理用户缓存信息
     * 用户信息变更时
     */
    @CacheEvict(cacheNames = "user-", key = "#username")
    public void cleanUserCache(String username) {
//        System.out.println("hello andCache delete"+user.getUserId())
    }
}