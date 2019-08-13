package com.bizseer.auth.util.jwt;

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

@Component("BackdoorChecker")
public class BackdoorChecker extends GenericFilterBean {
    // 功能：检查header中是否有二方服务的token，有则鉴权通过。
    private static final String AUTH_HEADER_FIELD = "Authorization";
    private static final String backdoorToken = "RYZ2TmkPPfiBcmWI713hWhiE4AQfAdcF";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }
        String token = ((HttpServletRequest) request).getHeader(AUTH_HEADER_FIELD);
        if (backdoorToken.equals(token)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("backdoor", null, null));
        }
        chain.doFilter(request, response);
    }
}
