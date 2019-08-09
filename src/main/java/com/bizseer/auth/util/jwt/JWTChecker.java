package com.bizseer.auth.util.jwt;

import com.bizseer.auth.constant.AuthRoleType;
import com.bizseer.auth.constant.ConstAuth;
import com.bizseer.auth.repository.AuthRepository;
import com.bizseer.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private AuthService authService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 请求的头部存在有效jwt，则向holder中加入一个authentication
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String username = JWTHelper.parseUsernameFromRequest(httpServletRequest);
        // if jwt validate failed
        AuthRoleType userAuthRole = AuthRoleType.getAuthRoleByName((String) authService.getUserOneInfoByUsername(ConstAuth.ROLE,username));
        // 判断用户拥有访问url的权限
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

}
