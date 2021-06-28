package com.yh20studio.springbootwebservice.component;

import com.yh20studio.springbootwebservice.domain.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {

    private SecurityUtil() { }

    public static Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            //401 Error
            throw new RestException(HttpStatus.UNAUTHORIZED, "Security Context에 인증 정보가 없습니다.");
        }
        return Long.parseLong(authentication.getName());
    }
}
