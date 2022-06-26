/*package com.example.nosql.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    //    CustomUsernamePasswordAuthenticationFilter myAuthFilter = new CustomUsernamePasswordAuthenticationFilter();
    //    myAuthFilter.setAuthenticationManager(authenticationManager(new AuthenticationConfiguration()));

        //http.cors();
        //http.csrf().disable();
        //http.httpBasic();
        http.formLogin().disable();
        http.cors().and()

                .authorizeHttpRequests()
                .antMatchers("/connect/**").permitAll()
                .antMatchers("/registration**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}*/
