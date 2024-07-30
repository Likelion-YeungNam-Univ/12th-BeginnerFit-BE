package com.example.beginnerfitbe.jwt.util;

import com.example.beginnerfitbe.redis.service.RedisService;
import com.example.beginnerfitbe.user.service.UserService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secretKey;
    private final long accessExpiration;
    private final long refreshExpiration;
    private final String issuer;
    private final UserService userService;
    private final RedisService redisService;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.accessExpiration}") long accessExpiration,
            @Value("${jwt.refreshExpiration}") long refreshExpiration,
            @Value("${jwt.issuer}") String issuer,
            UserService userService,
            RedisService redisService
    ) {
        this.secretKey = secretKey;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.issuer = issuer;
        this.userService = userService;
        this.redisService = redisService;
    }

    public String generateAccessToken(String email, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String generateRefreshToken(String email) {
        String refreshToken = Jwts.builder()
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
        redisService.setDataExpire(email, refreshToken, refreshExpiration);
        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            if (userService.read(claims.getBody().get("userId", Long.class)) == null) {
                return false;
            }
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
        }
        return false;
    }

    public boolean validateTokenExceptExpiration(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void validateRefreshToken(String email, String refreshToken) {
        String redisRefreshToken = redisService.getData(email);
        if (!refreshToken.equals(redisRefreshToken)) {
            throw new IllegalArgumentException("유저 정보 일치 x");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", Long.class);
    }

    public boolean deleteRegisterToken(String email){
        try{
            if(redisService.hasKey(email)){
                redisService.deleteData(email);
                return true;
            }
        }
        catch (Exception e){e.printStackTrace();}
        return false;
    }
}
