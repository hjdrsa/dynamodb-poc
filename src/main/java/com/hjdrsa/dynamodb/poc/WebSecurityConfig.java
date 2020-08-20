package com.hjdrsa.dynamodb.poc;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author hjd
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    System.out.println(httpSecurity);
    httpSecurity
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/actuator/**")
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
  }
}
