//package com.nhnacademy.mini_dooray.ssacthree_front.elastic;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@TestConfiguration
//@EnableWebSecurity
//public class TestSecurityConfig {
//
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
//
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.inMemoryAuthentication()
//        .withUser("username")
//        .password(passwordEncoder().encode("password"))
//        .roles("USER");
//  }
//
//  protected void configure(HttpSecurity http) throws Exception {
//    http.csrf().disable()
//        .authorizeRequests()
//        .anyRequest().authenticated()
//        .and()
//        .httpBasic();
//  }
//}
