package com.softvision.bank.tdd.security.jwt;

import com.softvision.bank.tdd.exceptions.ForbiddenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${com.softvision.library.tdd.jwt.secret}")
    private String jwtSecret;
    @Value("${com.softvision.library.tdd.jwt.expirationMs}")
    private Long jwtExpirationMs;

    public String generateJwt(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date dateNow = new Date();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(dateNow)
                .setExpiration(new Date(dateNow.getTime() + jwtExpirationMs))
                .claim("roles", userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            throw new ForbiddenException(e.getMessage());
        }
    }
}
