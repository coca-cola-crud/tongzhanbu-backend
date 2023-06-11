package com.shu.tongzhanbu.security;


import com.shu.tongzhanbu.service.UserCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


/**
 * @author DELL
 */
@Service
@RequiredArgsConstructor
public class ConsumerDetailsService implements UserDetailsService {

    private final UserCacheManager userCacheManager;


    @Override
    public UserDetails loadUserByUsername(String account) {
        return userCacheManager.getUserCache(account);
    }

}
