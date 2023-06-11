package com.shu.tongzhanbu.component.common;

/**
 * @author tangyanqing
 * Description:
 * Date: 2021-08-20
 * Time: 10:24
 */
public class Const {


    public static String STATE_PARAM = "state";
    public static String REDIRECT_STATE_PREFIX = "state_redirect:";
    //登录
    public static String LOGIN_CALLBACK_URL = "/oauth2/callback/*";
    public static String LOGIN_CALLBACK_TOKEN = "token";
    public static String LOGIN_REDIRECT_PARAMETER_NAME = "redirect";

    // 权限
    public static String AUTHORIZATION_HEADER = "Authorization";

    public static String SYSTEM_ROLE_ADMIN = "ROLE_admin";
    public static String SYSTEM_ROLE_USER = "ROLE_user";
    public static String SYSTEM_ROLE_DEP = "ROLE_dep";
    public static String SYSTEM_ROLE_ANONYMOUS = "ROLE_ANONYMOUS";


    public static String SYSTEM_PERMISSION_ALL = "/**";
    public static String SYSTEM_PERMISSION_PERMIT_ANONYMOUS1 = "/login/shu";
    public static String SYSTEM_PERMISSION_PERMIT_ANONYMOUS2 = "/getUserLoginInfo";

    public static String SYSTEM_PERMISSION_USER1 = "/shurole/**";
    public static String SYSTEM_PERMISSION_USER2 = "/user/userListByRealName";


}
