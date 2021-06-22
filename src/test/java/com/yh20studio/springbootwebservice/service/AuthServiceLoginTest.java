package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.JwtUtil;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshTokenRepository;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenResponseDto;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application-jwt.yml" +
        ",classpath:/application-google.yml" +
        ",classpath:/application-postgresqltest.yml"
)
public class AuthServiceLoginTest {

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberSaveRequestDto memberSaveRequestDto;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @SpyBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @After("")
    public void cleanup (){
        memberRepository.deleteAll();
    }

    @Test
    public void Member_DTO_login_WithRightData() throws RuntimeException {
        //given
        TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
                .grantType("grantType")
                .accessToken("accessToken")
                .accessTokenExpiresIn(1L)
                .refreshToken("refreshToken")
                .refreshTokenExpiresIn(1L)
                .build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("test@naver.com", "123");

        Member savedMember = new Member("test", "test@naver.com", "none", "Google", passwordEncoder.encode("123"), Member.Role.GUEST);

        RefreshToken refreshToken = RefreshToken.builder()
                .key("1")
                .value("refreshToken")
                .expires(1L)
                .build();

        given(memberSaveRequestDto.toAuthentication()).willReturn(authenticationToken);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(savedMember));
        given(jwtUtil.generateTokenDto(isA(Authentication.class))).willReturn(tokenResponseDto);
        given(refreshTokenRepository.save(isA(RefreshToken.class))).willReturn(refreshToken);

        //when
        TokenResponseDto retunTokenResponseDto = authService.login(memberSaveRequestDto);

        //then
        Assertions.assertNotNull(retunTokenResponseDto);
        verify(memberSaveRequestDto, times(1)).toAuthentication();
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(jwtUtil, times(1)).generateTokenDto(isA(Authentication.class));
        verify(refreshTokenRepository, times(1)).save(isA(RefreshToken.class));

    }

}
