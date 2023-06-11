package com.shu.tongzhanbu.security.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author tangyanqing
 * Description:
 * Date: 2019-08-29
 * Time: 14:36
 */
@Component
public class MyLogoutHandler implements LogoutHandler {

    @Value("${properties.logoutUrl}")
    private String logoutUrl;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        try {
            HttpSession session = httpServletRequest.getSession();
            session.invalidate();
            SecurityContextHolder.clearContext();
            httpServletResponse.sendRedirect(logoutUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyLogoutHandler() {

    }
}
