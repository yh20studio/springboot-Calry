package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.JwtUtil;
import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackList;
import com.yh20studio.springbootwebservice.domain.accessTokenBlackList.AccessTokenBlackListRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshTokenRepository;
import com.yh20studio.springbootwebservice.dto.httpResponse.MessageResponse;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.AccessTokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class AuthService {
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private RefreshTokenRepository refreshTokenRepository;
    private AccessTokenBlackListRepository accessTokenBlackListRepository;

    @Transactional
    public Long signup(MemberSaveRequestDto memberSaveRequestDto) {
        if(memberRepository.existsByEmail(memberSaveRequestDto.getEmail())){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member member = memberSaveRequestDto.toMember(passwordEncoder);

        return memberRepository.save(member).getId();
    }

    @Transactional
    public TokenResponseDto login(MemberSaveRequestDto memberSaveRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = memberSaveRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponseDto tokenResponseDto = jwtUtil.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenResponseDto.getRefreshToken())
                .expires(tokenResponseDto.getRefreshTokenExpiresIn())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenResponseDto;
    }

    @Transactional
    public MessageResponse logout(TokenRequestDto tokenRequestDto) {

        if (!jwtUtil.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = jwtUtil.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // AccessToken 블랙리스트 추가
        AccessTokenRequestDto accessTokenRequestDto = AccessTokenRequestDto.builder()
                .key(refreshToken.getKey())
                .value(tokenRequestDto.getAccessToken())
                .expires(tokenRequestDto.getAccessTokenExpiresIn())
                .build();

        accessTokenBlackListRepository.save(accessTokenRequestDto.toEntity());

        // RefreshToken 삭제
        refreshTokenRepository.deleteByKey((refreshToken.getKey()));

        return new MessageResponse("Logout");
    }

    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!jwtUtil.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtUtil.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenResponseDto tokenResponseDto = jwtUtil.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken
                .updateValue(tokenResponseDto.getRefreshToken())
                .updateExpires(tokenResponseDto.getRefreshTokenExpiresIn());

        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenResponseDto;
    }
}
