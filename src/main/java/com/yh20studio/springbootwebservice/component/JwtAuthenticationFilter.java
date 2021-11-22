package com.yh20studio.springbootwebservice.component;

import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackListRepository;
import com.yh20studio.springbootwebservice.exception.RestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//final이나 @NonNull인 필드 값만 파라미터로 받는 생성자를 만들어줍니다.
@RequiredArgsConstructor
/*
    JwtAuthenticationFilter는 사용자가 요청할 때마다 1번씩만 검증하면 되는 Filter이다.
    따라서 OncePerRequestFilter에 JwtAuthentication을 검증하는 시스템을 추가한다.
    (OncePerRequestFilter는 그 이름에서도 알 수 있듯이 모든 서블릿에 일관된 요청을 처리하기 위해 만들어진 필터이며, 같은 요청이 다른 여러개의 서블릿을 불러오더라도
     OncePerRequestFilter는 딱 1번만 Filtering을 하도록 되어 있다.)
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static String AUTHORIZATION_HEADER = "Authorization";
    public static String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final AccessTokenBlackListRepository accessTokenBlackListRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
            // 만약 해당 토큰이 금지 되었다면 HttpStatus.UNAUTHORIZED Throw
            if (accessTokenBlackListRepository.existsByValue(jwt)) {
                //401 Error
                throw new RestException(HttpStatus.UNAUTHORIZED, "Access Token 이 유효하지 않습니다.");
            } else {
                Authentication authentication = jwtUtil.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    // request Header에서 bearerToken을 가져오고 jwt만 자른다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
