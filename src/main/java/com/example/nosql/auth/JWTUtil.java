package com.example.nosql.auth;

import com.example.nosql.schema.UsersDB;

import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    private  Logger logger = LogManager.getLogger(JWTUtil.class);

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;  //24h expired token;
    @Value("${app.jwt.secret}")
    private String secretKey ;

    public String generateToken(UsersDB usersDB) {

        return Jwts.builder()
                .setSubject(usersDB.getUsername())
                .setIssuer("NoSQL_DB")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

    public boolean validateAccessToken(String token) {

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException ex) {
            logger.error("JWT expired:",ex);
        }catch (MalformedJwtException ex) {
            logger.error("jwt is invalid:",ex);
        }catch (IllegalArgumentException ex) {
            logger.error("jwt is null,empty or has whitespace:",ex);
        }catch (UnsupportedJwtException ex) {
            logger.error("jwt is unsupported :",ex);
        }catch (SignatureException ex) {
            logger.error("jwt signature error:",ex);
        }
        return false;

    }

    public String getSubjectToken(String token) {

        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

    }

}
