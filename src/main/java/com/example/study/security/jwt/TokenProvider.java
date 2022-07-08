package com.example.study.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final String secretKey;
    private static final String AUTHORITIES_KEY = "auth";
    private static final long accessExpireTime = 30 * 60 * 1000; // 30분
    private static final long refeshExpireTime = 60 * 60 * 1000 * 24 * 7; // 7일

    private Key key;

    public TokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToke(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰 유효기간 설정
        Date expiration = new Date(new Date().getTime() + accessExpireTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiration)
                .compact();
    }

//    public String createRefreshToken(LoginDTO loginDTO) {
//    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    public boolean validateJwtToken(ServletRequest request, String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 토큰 서명");
            request.setAttribute("exception", "잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰");
            request.setAttribute("exception", "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰");
            request.setAttribute("exception", "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("잘못된 토큰");
            request.setAttribute("exception", "JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
