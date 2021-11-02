package com.yh20studio.springbootwebservice.component;

import com.yh20studio.springbootwebservice.exception.RestException;
import com.yh20studio.springbootwebservice.dto.token.AccessTokenResponseDto;
import com.yh20studio.springbootwebservice.dto.token.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static String AUTHORITIES_KEY = "auth";
    private static String BEARER_TYPE = "bearer";
    private static long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 1;   // 1일
    private static long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    private Key key;

    // JWT Secret key
    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Authentication으로 토큰 생성
    public TokenDto generateToken(Authentication authentication){

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date().getTime());

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Refresh Token 생성
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                .build();

    }

    public AccessTokenResponseDto reissueAccessToken(Authentication authentication){

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date().getTime());

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return AccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .build();

    }

    // AccessToken으로 Authentication 생성
    public Authentication getAuthentication(String accessToken){

        Claims claims = parseClaims(accessToken);

        // claims 값에  AccessToken을 생성할 때 넣었던 authorities 값이 존재하지 않는다면 HttpStatus.UNAUTHORIZED Trow
        if (claims.get(AUTHORITIES_KEY) == null){
            //401 Error
            throw new RestException(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // username 값에 memberId 값을 넣어줌
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
        // 이 토큰은 id/password로 로그인한 Token이 아니라 jwtFilter를 거치고 온 요청에서 입력없이 로그인이 되는 과정
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    // 토큰의 유효성 확인
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){

        } catch (ExpiredJwtException e) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        } catch (UnsupportedJwtException e) {


        } catch (IllegalArgumentException e) {

        }
        return false;
    }

    // 토큰의 claims 확인
    public Claims parseClaims(String accessToken){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (MalformedJwtException e) {
            throw new RestException(HttpStatus.UNAUTHORIZED, "토큰 형식이 아닙니다.");
        }
        catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
