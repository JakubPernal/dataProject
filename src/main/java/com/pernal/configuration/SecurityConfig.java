package com.pernal.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${service.data.client.login}")
    private String clientLogin;

    @Value("${service.data.client.password}")
    private String clientPassword;

    @Value("${service.data.admin.login}")
    private String adminLogin;

    @Value("${service.data.admin.password}")
    private String adminPassword;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                    .withUser(clientLogin)
                    .password("{noop}" + clientPassword)
                    .authorities("ROLE_USER")
                .and()
                    .withUser(adminLogin)
                    .password("{noop}" + adminPassword)
                    .authorities("ROLE_ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/data/upload", "/data/get/**").hasAuthority("ROLE_USER")
                .and()
                    .authorizeRequests()
                    .antMatchers("/data/delete/**").hasAuthority("ROLE_ADMIN")
                .and()
                    .httpBasic();
    }

}
