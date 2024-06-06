package com.example.Redi.security;

import com.example.Redi.users.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenFilter JwtTokenFilter;
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public void  configure(HttpSecurity http) throws Exception {



        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                        }
                )
                .and();

        http.authorizeRequests()
                // Swagger endpoints must be publicly accessible
                .mvcMatchers(AUTH_WHITELIST).permitAll()
                .mvcMatchers("/user/create").hasAnyRole(String.valueOf(Role.ADMIN))
                .mvcMatchers("/user/login").permitAll()
                .mvcMatchers("/user/admin/all").hasAnyRole(String.valueOf(Role.ADMIN))
                .mvcMatchers("/product/admin/get-all").hasAnyRole(String.valueOf(Role.ADMIN))
                .mvcMatchers("/product/admin/delete").hasAnyRole(String.valueOf(Role.ADMIN))
                .anyRequest().authenticated();
        // Add JWT token filter
        http.addFilterBefore(JwtTokenFilter, UsernamePasswordAuthenticationFilter.class);



    }
}
