package ru.example.auth.config.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Service
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtAccessTokenFilter jwtAccessTokenFilter;

    @Autowired
    public JwtConfigurer(JwtAccessTokenFilter jwtAccessTokenFilter) {
        this.jwtAccessTokenFilter = jwtAccessTokenFilter;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        //ставим наш фильтр перед системным
        builder.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
