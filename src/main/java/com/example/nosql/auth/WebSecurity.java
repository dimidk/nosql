package com.example.nosql.auth;

import com.example.nosql.MasterDB;
import com.example.nosql.shared.SharedClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(
        prePostEnabled = false,
        securedEnabled = false,
        jsr250Enabled = true
)
public class WebSecurity extends WebSecurityConfigurerAdapter {

   // @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MasterDB userDatabase;

    private SharedClass sharedClass = new SharedClass(restTemplate);
    @Autowired
    private JWTFilter jwtFilter;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

   protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(username -> {
            return sharedClass.fromJsonUser(username,userDatabase);
        });
    }

    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling().authenticationEntryPoint(
                ((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
                })
        );

        http.authorizeHttpRequests()
                .antMatchers("/auth/connect").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);

    }
}
