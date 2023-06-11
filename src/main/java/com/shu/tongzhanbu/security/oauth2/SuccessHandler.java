package com.shu.tongzhanbu.security.oauth2;


import com.shu.tongzhanbu.component.common.Const;
import com.shu.tongzhanbu.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tangyanqing
 * Description:
 * Date: 2020-02-27
 * Time: 14:59
 */
@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;


    @Value("${properties.webUrl}")
    private String callBackUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        System.out.println("认证成功");
        String jwt = tokenProvider.createToken(authentication);
        String redirect = request.getParameter(Const.STATE_PARAM);
        if (redirect != null && redirect.startsWith(Const.REDIRECT_STATE_PREFIX)) {
            redirect = redirect.replace(Const.REDIRECT_STATE_PREFIX, "");
            response.sendRedirect(callBackUrl + redirect + "?" + Const.LOGIN_CALLBACK_TOKEN + "=" + jwt);
        } else {
            response.sendRedirect(callBackUrl + "?" + Const.LOGIN_CALLBACK_TOKEN + "=" + jwt);
        }
    }
}
