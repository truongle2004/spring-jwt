package com.example.todoapp.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${pp.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // Generate JWT Token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        // JWT Token
        // add more dependency here to use JWTs
//        <dependency>
//        <groupId>io.jsonwebtoken</groupId>
//        <artifactId>jjwt</artifactId>
//        <version>0.2</version>
//    </dependency>
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // get username form jwt token
    public String getUsername(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwtSecret)
                .getBody();
        // return username
        return claims.getSubject();
    }

    // validate jwt token
    public boolean validateToken(String token) {
        try {


            Jwts.parserBuilder()// build token
                    .setSigningKey(key())// set the signing key for jwt
                    .build() // finally process of jwt parser
                    .parse(token); // parse jwt from token, checks it's signature
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
