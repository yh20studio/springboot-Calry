package com.yh20studio.springbootwebservice.config;

import com.yh20studio.springbootwebservice.component.JwtAccessDeniedHandler;
import com.yh20studio.springbootwebservice.component.JwtAuthenticationEntryPoint;
import com.yh20studio.springbootwebservice.component.JwtUtil;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.service.CustomOAuth2UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomOAuth2UserService customOAuth2UserService;
    private JwtUtil jwtUtil;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

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
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                    .headers().frameOptions().disable()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/archives/**", "/css/**", "/login**", "/auth/**", "/images/**", "/js/**", "/h2/**", "/h2-console/**", "/favicon.ico").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Member.Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    .apply(new JwtSecurityConfig(jwtUtil))
                .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService);



    }


}
