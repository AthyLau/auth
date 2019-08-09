
package com.bizseer.auth.util.jwt;

import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JWTHelper {
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24 * 7;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final String SECRET = "SI6IkpvaG4gRG9lIiwiYWRt";

    public static Map<String, Object> getLoginResponse(String username) {
        // 生成一个header
        return ImmutableMap.of(HEADER_STRING, generateToken(username));
    }

    public static String parseUsernameFromRequest(HttpServletRequest request) {
        return parseUsernameFromToken(request.getHeader(HEADER_STRING));
    }

    private static String generateToken(String username) {
        // 生成用户、权限有效时间为2小时的jwttoken
        JwtBuilder jwtBuilder = Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
            .signWith(SignatureAlgorithm.HS512, SECRET);
        return TOKEN_PREFIX + jwtBuilder.compact();
    }

    public static String parseUsernameFromToken(String token) {
        if (token == null) {
            return null;
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            log.warn("jwt expired or illegal, the token: {}, warn message: {}", token, e.getMessage());
        }
        return null;
    }

}
