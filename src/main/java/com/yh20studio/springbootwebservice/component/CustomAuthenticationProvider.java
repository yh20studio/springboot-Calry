package com.yh20studio.springbootwebservice.component;

import com.yh20studio.springbootwebservice.exception.RestException;
import com.yh20studio.springbootwebservice.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        if (authentication == null) {
            throw new InternalAuthenticationServiceException("Authentication is null");
        }

        String username = authentication.getName();

        if (authentication.getCredentials() == null) {
            throw new AuthenticationCredentialsNotFoundException("Credentials is null");
        }

        String password = authentication.getCredentials().toString();
        UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);

        if (!loadedUser.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }

        if (!loadedUser.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

        // 실질적인 인증
        if (!loadedUser.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }

        // checker
        if (!passwordEncoder.matches(password, loadedUser.getPassword())) {
            throw new RestException(HttpStatus.FORBIDDEN, "비밀번호가 틀렸습니다.");
        }

        // 인증 완료
        if (!loadedUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
            loadedUser, null, loadedUser.getAuthorities());

        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        //return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
