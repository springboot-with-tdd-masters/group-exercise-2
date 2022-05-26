package com.group3.exercise.bankapp.security.jwt;

import com.group3.exercise.bankapp.security.entities.UserWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private final String salt;
    private final Long validity;
    private final String issuer;

    public JwtTokenUtil(@Value("${jwt.salt}") String salt,@Value("${jwt.validity}") Long validity,@Value("${jwt.issuer}") String issuer){
        this.salt = salt;
        this.validity = validity;
        this.issuer = issuer;
    }
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    // generic getter
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return new Date().after(expiration);
    }

    // parser.
    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(salt)
                .parseClaimsJws(token)
                .getBody();
    }
    // generator
    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user.getUsername());
    }
    public String generateToken(UserWrapper user) {
        Map<String, Object> claims = new HashMap<>();
        // put user id reference for other services.
        claims.put("userId", user.getUserId());
        // put issuer
        claims.put("iss", issuer);

        return doGenerateToken(claims, user.getUsername());

    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
                .signWith(SignatureAlgorithm.HS512, salt)
                .compact();
    }
    // validator
    public Boolean validateToken(String token, UserDetails user) {
        final String userName = getUsernameFromToken(token);
        return userName.equals(user.getUsername()) &&
                isTokenExpired(token);
    }
}
