package com.pernal.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                    .withUser("client")
                    .password("{noop}Client123.")
                    .authorities("ROLE_USER")
                .and()
                    .withUser("serviceOwner")
                    .password("{noop}Owner123.")
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
