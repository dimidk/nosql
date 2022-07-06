package com.example.nosql.auth;

import com.example.nosql.schema.UsersDB;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JWTUtil {

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

}
