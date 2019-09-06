package com.bizseer.auth.service;

import com.bizseer.auth.config.Config;
import com.bizseer.auth.constant.AuthRoleType;
import com.bizseer.auth.constant.AuthType;
import com.bizseer.auth.constant.ConstAuth;
import com.bizseer.auth.repository.AuthRepository;
import com.bizseer.auth.util.authenticator.AbstractAuth;
import com.bizseer.auth.util.authenticator.AuthFactory;
import com.bizseer.auth.util.exception.AuthException;
import com.bizseer.auth.util.exception.UserExistException;
import com.bizseer.auth.util.exception.UserKeyNotFoundException;
import com.bizseer.auth.util.exception.UserRoleException;
import com.bizseer.auth.util.jwt.JWTHelper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 10:47 AM
 * @since JDK 1.8
 */
@Service
public class AuthService {

    private static List<String> rolenames;
    private static Map<String, Object> defaultUser;
    @Autowired
    private AuthRepository authRepository;
    private AuthType authType = AuthType.NONE;
    @Autowired
    private AuthFactory authFactory;

    private AbstractAuth authenticator;

    static {
        defaultUser = new HashMap<String, Object>() {{
            put(ConstAuth.USERNAME, "aiops");
            put(ConstAuth.PASSWORD, "aiops");
            put(ConstAuth.ROLE, AuthRoleType.SUPER_ADMIN.getName());
        }};
        rolenames = new ArrayList<String>() {{
            for (AuthRoleType role : AuthRoleType.values()) {
                add(role.getName());
            }
        }};
    }

    /**
     * 根据配置文件的信息刷新Authenticator（目前在config里写死了self）
     *
     * @param config 加载的配置
     * @return boolean
     */
    public boolean refreshAuthenticator(Config config) {
        if (config.authType != this.authType) {
            // 调整了authType
            this.authType = config.authType;
            this.authenticator = authFactory.getAuthenticator(authType);
            return true;
        }
        return false;
    }

    public boolean inspectionAuth(String... roles) {
        for (String role : roles) {
            if (!AuthService.rolenames.contains(role)) {
                return false;
            }
        }
        return true;
    }

    public Map<String, Object> login(Map<String, Object> user) {
        if (user == null) {
            throw new AuthException("login user can't be null");
        }
        String username = (String) user.getOrDefault(ConstAuth.USERNAME, "");
        String passowrd = (String) user.getOrDefault(ConstAuth.PASSWORD, "");
        String role = (String) user.getOrDefault(ConstAuth.ROLE, null);
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(passowrd)) {
            throw new AuthException("username and password can't be null");
        }
        if (role != null && !inspectionAuth(role)) {
            throw new AuthException("role is wrong");
        }
        Map<String, Object> userInDB = authRepository.getUser(username);
        String passwordSha = (String) userInDB.getOrDefault(ConstAuth.PASSWORD_SHA, "");
        if (passwordSha.equals(AuthRepository.getShaString(passowrd))) {
            return JWTHelper.getLoginResponse(username);
        } else {
            return null;
        }
    }

    /**
     * 注册
     *
     * @param jwtHeader
     * @param body
     * @return
     */
    public boolean register(String jwtHeader, Map<String, String> body) throws AuthException {
        String loginUserName = JWTHelper.parseUsernameFromToken(jwtHeader);
        if (!Strings.isNullOrEmpty(loginUserName) && isUserExist(loginUserName)) {
            Integer power = getPowerByUsername(loginUserName);
            if (power < 10) {
                return false;
            }
        }
        String username = body.getOrDefault(ConstAuth.USERNAME, "");
        String passowrd = body.getOrDefault(ConstAuth.PASSWORD, "");
        String role = body.getOrDefault(ConstAuth.ROLE, "");
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(passowrd) || Strings.isNullOrEmpty(role)) {
            throw new AuthException("Register information can not be null!");
        }
        Integer power = getPowerByRole(role);
        return power != null && power != 10 && register(username, passowrd, role);
    }

    public boolean register(String username, String password, String role) throws AuthException {
        //角色不存在
        if (!inspectionAuth(role)) {
            throw new UserRoleException("The user role is not exist!");
        }
        //用户已经存在
        if (isUserExist(username)) {
            throw new UserExistException("The user is not exist!");
        }
        authRepository.register(username, password, role);
        return true;
    }


    public boolean isUserExist(String username) {
        Map<String, Object> user = authRepository.getUser(username);
        return user != null;
    }

    public Integer getPowerByUsername(String username) {
        String role = (String) getUserOneInfoByUsername(ConstAuth.ROLE, username);
        return getPowerByRole(role);
    }

    public Integer getPowerByRole(String role) {
        AuthRoleType authRoleType = AuthRoleType.getAuthRoleByName(role);
        if (authRoleType != null) {
            return authRoleType.getPower();
        }
        return null;
    }

    //这边可以用原型模式来设计
    public Object getUserOneInfoByUsername(String infoName, String username) throws AuthException {
        if (!defaultUser.containsKey(infoName)) {
            throw new UserKeyNotFoundException(infoName + " key is not exist！");
        }
        Map<String, Object> user = authRepository.getUser(username);
        if (user != null) {
            return user.getOrDefault(infoName, null);
        }
        return null;
    }

}

















