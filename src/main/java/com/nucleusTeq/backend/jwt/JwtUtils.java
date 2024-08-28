package com.nucleusTeq.backend.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret ;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public  String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove Bearer prefix
        }
        return null;

    }

    public  String generateTokenFromUsername(UserDetails userDetails) {

        String username = userDetails.getUsername();
        System.out.println(username);

        Map<String , Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .and()
                .signWith(key())
                .compact();


    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }




    private Key key() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public  boolean validateJwtToken(String authToken) {
        try {

            System.out.println("Validate");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        }  catch (
    MalformedJwtException e) {
        logger.debug(e.getMessage(), Level.parse("Invalid JWT token: {}"));
    } catch (
    ExpiredJwtException e) {
        logger.debug(e.getMessage(), Level.parse("JWT token is expired: {}"));
    } catch (
    UnsupportedJwtException e) {
        logger.debug(e.getMessage(), Level.parse("JWT token is unsupported: {}"));
    } catch (IllegalArgumentException e) {
        logger.debug(e.getMessage(), Level.parse("JWT claims string is empty: {}"));
    }
        return  false;
    }




}
