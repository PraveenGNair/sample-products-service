package com.samples.products.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${spring.security.user.name}")
  private String username;

  @Value("${spring.security.user.password}")
  private String encryptedPassword;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .passwordEncoder(passwordEncoder())
        .withUser(username).password(encryptedPassword).roles("USER");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        // Disable CSRF (not called via a web page).
        .csrf().disable()
        .authorizeRequests()
        // Allow unauthenticated access to the healthcheck.
        .antMatchers("/actuator/health").permitAll()
        .antMatchers("/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**").permitAll()
        // Require authentication on all other requests.
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .and().headers().frameOptions().sameOrigin();
  }

}