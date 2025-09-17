package com.moira.bingobackend.global.auth;

import com.moira.bingobackend.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration-time.atk}")
    private Long expirationTimeOfAtk;

    @Value("${jwt.expiration-time.rtk}")
    private Long expirationTimeOfRtk;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretKey)
        );
    }

    private String createToken(User user, Long expirationTime) {
        // claim
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        claims.put("nickname", user.getNickname());

        // time
        Date now = new Date();

        // create token
        return Jwts.builder()
                .subject(user.getId())
                .claims(claims)
                .expiration(new Date(now.getTime() + expirationTime))
                .issuedAt(now)
                .issuer(issuer)
                .signWith(key)
                .compact();
    }

    public String createAtk(User user) {
        return createToken(user, expirationTimeOfAtk);
    }

    public String createRtk(User user) {
        return createToken(user, expirationTimeOfRtk);
    }

    public String substringToken(String authorizationHeaderValue) {
        return authorizationHeaderValue.substring("Bearer ".length());
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
