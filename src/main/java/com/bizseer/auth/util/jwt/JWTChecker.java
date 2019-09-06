package com.bizseer.auth.util.jwt;

import com.bizseer.auth.constant.AuthRoleType;
import com.bizseer.auth.constant.ConstAuth;
import com.bizseer.auth.repository.AuthRepository;
import com.bizseer.auth.service.AuthService;
import com.bizseer.auth.util.exception.AuthException;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component("JWTChecker")
public class JWTChecker extends GenericFilterBean {
    private static final String AUTH_HEADER_FIELD = "Authorization";
    @Autowired
    private AuthService authService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 请求的头部存在有效jwt，则向holder中加入一个authentication
//        ModifyRequestHeaderWrapper requestHeaderWrapper = new ModifyRequestHeaderWrapper((HttpServletRequest) request);
        String username = JWTHelper.parseUsernameFromRequest((HttpServletRequest) request);
        if (Strings.isNullOrEmpty(username) && !((HttpServletRequest) request).getRequestURI().contains("/login")
                && ((HttpServletRequest) request).getHeader(BackdoorChecker.AUTH_HEADER_FIELD).equals(BackdoorChecker.backdoorToken) ) {
            throw new AuthException("Token is error! Please login!");
        }
        chain.doFilter(request, response);
    }

}
