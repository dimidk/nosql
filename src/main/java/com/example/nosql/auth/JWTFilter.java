package com.example.nosql.auth;

import com.example.nosql.schema.UsersDB;
import com.example.nosql.schema.UsersDBToken;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!hasAuthorizationHeader(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getAccessToken(request);
        if (!jwtUtil.validateAccessToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        setAuthenticationContext(accessToken,request);
        filterChain.doFilter(request, response);


    }

    private void setAuthenticationContext(String accessToken,HttpServletRequest request) {

        UserDetails userDetails = getUserDetails(accessToken);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String accessToken) {
        //UsersDBToken userDetails = new UsersDBToken();
        UsersDB userDetails = new UsersDB();

        Claims claims = jwtUtil.parseClaims(accessToken);
        String username = (String) claims.get(Claims.SUBJECT);
        String roles = (String) claims.get("roles");
 
    //    String username = jwtUtil.getSubjectToken(accessToken);
        userDetails.setUsername(username);
       // userDetails.addRole();
        userDetails.setRole(roles);

        return userDetails;
    }
    private boolean hasAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        logger.info("Authorization:"+header);

        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }

        return true;
    }

    private String getAccessToken(HttpServletRequest request) {

        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        logger.info("access Token from request is "+token);
        return token;
    }
}
