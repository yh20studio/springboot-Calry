package com.yh20studio.springbootwebservice.config;

import com.yh20studio.springbootwebservice.domain.user.User;
import com.yh20studio.springbootwebservice.service.CustomOAuth2UserService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        /**http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/test", "/h2-console/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().headers().frameOptions().sameOrigin()
                .and().csrf().disable();**/
        http
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/archives/**", "/css/**", "/login**", "/images/**", "/js/**", "/h2/**", "/h2-console/**", "/favicon.ico").permitAll()
                    .antMatchers("/api/v1/**").hasRole(User.Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService);


    }


}
