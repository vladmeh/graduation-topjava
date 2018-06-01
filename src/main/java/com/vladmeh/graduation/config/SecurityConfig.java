package com.vladmeh.graduation.config;

import com.vladmeh.graduation.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Vladimir Mikhaylov <vladmeh@gmail.com> on 24.05.2018.
 * @link https://github.com/vladmeh/graduation-topjava
 *
 * http://www.baeldung.com/spring-security-authentication-with-a-database
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //http://www.baeldung.com/spring-security-authentication-with-a-database
    private final UserDetailService userDetailService;

    @Autowired
    public SecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("/api-dev/**").hasAuthority(Role.ROLE_USER.getAuthority()).anyRequest().authenticated()
                .antMatchers("/api-dev/**").authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .httpBasic()
                .and().csrf().disable();

        //http://www.baeldung.com/spring-security-session
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
    }
}
