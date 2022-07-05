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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.example.auth.config.security.jwt.JwtConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(// теперь доступы прописываются над методами
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /////////////////////////////
    // BY JWT TOKEN
    private final JwtConfigurer jwtConfigurer;

    @Qualifier("userDetailsServiceImpl")
    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;

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
                //проверить доступность имени через сервис (валидации)
                    .antMatchers(new AntPathRequestMatcher("/auth/api/v1/validate/username", HttpMethod.POST.name()).getPattern()).permitAll()
                    .antMatchers(new AntPathRequestMatcher("/auth/api/v1/registration", HttpMethod.POST.name()).getPattern()).permitAll()
                    .antMatchers(new AntPathRequestMatcher("/auth/api/v1/login", HttpMethod.POST.name()).getPattern()).permitAll()//max 10 за 1 час

                    .antMatchers(new AntPathRequestMatcher("/auth/api/v1/verify/token", HttpMethod.POST.name()).getPattern()).authenticated()
                    .antMatchers(new AntPathRequestMatcher("/auth/api/v1/refresh-token", HttpMethod.POST.name()).getPattern()).authenticated()
                    .antMatchers(new AntPathRequestMatcher("/auth/api/v1/logout", HttpMethod.POST.name()).getPattern()).authenticated()
                    .antMatchers("/api/v1/users/*").authenticated()
                .and()
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
//                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .apply(jwtConfigurer);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
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

    @Bean //!!!!!
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    //    /////////////////////////////
//    // BY DATABASE
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .anyRequest().authenticated()
//
//                .and()
//                .formLogin()
//                .loginPage(Route.AUTH_LOGIN).permitAll()
//                .defaultSuccessUrl(Route.AUTH_SUCCESS)
//
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID")
//                .logoutSuccessUrl(Route.AUTH_LOGIN);
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }
//
//    @Bean
//    protected DaoAuthenticationProvider daoAuthenticationProvider() {
//        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//
//        provider.setPasswordEncoder(passwordEncoder());
//        provider.setUserDetailsService(userDetailsService);
//
//        return provider;
//    }
//
//    @Bean
//    protected PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }

//    /////////////////////////////
//    // BY PERMISSIONS
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                    .antMatchers("/").permitAll()
//                // если над методом анн @PreAuthorize("hasAuthority('writer')") - то тут уже не указываем
////                    .antMatchers(HttpMethod.GET, "/api/**").hasAuthority(PermissionEnum.READER.getPermission())//"reader"
////                    .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(PermissionEnum.WRITER.getPermission())//"writer
////                    .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(PermissionEnum.WRITER.getPermission())//"writer
//                .anyRequest().authenticated()
//                    .and()
//                .httpBasic();
//    }
//
//    @Bean
//    protected UserDetailsService userDetailsService(){
//        return new InMemoryUserDetailsManager(
//                User.builder()
//                    .username("admin")
//                    .password(passwordEncoder().encode("admin"))
//                    .authorities(RoleEnum.ADMIN.getAuthorities())//SimpleGrantedAuthorities by "reader" and "writer"
//                    .build(),
//
//                User.builder()
//                    .username("user")
//                    .password(passwordEncoder().encode("user"))
//                    .authorities(RoleEnum.USER.getAuthorities())//SimpleGrantedAuthorities by "reader"
//                    .build()
//        );
//    }
//
//    @Bean
//    protected PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder(12);
//    }
}
