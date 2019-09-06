package com.bizseer.auth.util.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public static final String AUTH_HEADER_FIELD = "Authorization";
    public static final String backdoorToken = "RYZ2TmkPPfiBcmWI713hWhiE4AQfAdcF";
    private static final Logger logger = LoggerFactory.getLogger(BackdoorChecker.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(AUTH_HEADER_FIELD);
        if (backdoorToken.equals(token)) {
//            ModifyRequestHeaderWrapper requestHeaderWrapper = new ModifyRequestHeaderWrapper(((HttpServletRequest) request));
//            requestHeaderWrapper.putHeader(AUTH_HEADER_FIELD, JWTHelper.generateToken("aiops"));
            logger.info("Token is equals Backdoor");
//            chain.doFilter(requestHeaderWrapper, response);
        }
        chain.doFilter(request, response);
    }
}
