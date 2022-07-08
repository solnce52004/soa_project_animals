package ru.example.auth.config.security;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.example.auth.config.security.entrypoint.RestAuthenticationEntryPoint;
import ru.example.auth.config.security.jwt.JwtConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(// security annotations
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtConfigurer jwtConfigurer;

    @Qualifier("userDetailsServiceImpl")
    private final UserDetailsService userDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/swagger-ui/**").permitAll()
                .antMatchers("/auth/error/**").permitAll()
                .antMatchers(new AntPathRequestMatcher("/auth/api/v1/validate/username", HttpMethod.POST.name()).getPattern()).permitAll()
                .antMatchers(new AntPathRequestMatcher("/auth/api/v1/registration", HttpMethod.POST.name()).getPattern()).permitAll()
                .antMatchers(new AntPathRequestMatcher("/auth/api/v1/login", HttpMethod.POST.name()).getPattern()).permitAll()//max 10 за 1 час

                .antMatchers(new AntPathRequestMatcher("/auth/api/v1/verify/token", HttpMethod.POST.name()).getPattern()).authenticated()
                .antMatchers(new AntPathRequestMatcher("/auth/api/v1/refresh-token", HttpMethod.POST.name()).getPattern()).authenticated()
                .antMatchers(new AntPathRequestMatcher("/auth/api/v1/logout", HttpMethod.POST.name()).getPattern()).authenticated()
                .and()

                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()

                .apply(jwtConfigurer);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
