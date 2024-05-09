package com.example.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeHttpRequests(org.springframework.security.config.Customizer)
        // https://stackoverflow.com/a/76995493/1533065
        httpSecurity
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST, "/maintenance/**").hasRole("MANAGER")
                                .requestMatchers("/maintenance/**").hasAnyRole("MANAGER", "EMPLOYEE")
                                .anyRequest().permitAll()
                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/login")
                                .failureUrl("/login?failure")
                                .defaultSuccessUrl("/maintenance/product/display-list")
                )
                .exceptionHandling(handling ->
                        handling
                                .accessDeniedPage("/display-access-denied")
                );

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails sujeong = User.builder()
                .username("sujeong").password("{noop}sj123").roles("MANAGER").build();
        UserDetails ikhee = User.builder()
                .username("ikhee").password("{noop}ih123").roles("EMPLOYEE").build();
        UserDetails yeonjeong = User.builder()
                .username("yeonjeong").password("{noop}yj123").roles("GUEST").build();
        return new InMemoryUserDetailsManager(sujeong, ikhee, yeonjeong);
    }
}
