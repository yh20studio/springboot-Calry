package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.JwtUtil;
import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshToken;
import com.yh20studio.springbootwebservice.domain.refreshToken.RefreshTokenRepository;
import com.yh20studio.springbootwebservice.dto.archives.ArchivesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.token.TokenResponseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberSaveRequestDto memberSaveRequestDto;

    @InjectMocks
    private AuthService authService;

    private Member savedMember;

    @BeforeEach
    private void setup() {
        savedMember = new Member("test", "test@naver.com", "none", "Google", "123", Member.Role.GUEST);
        savedMember.updateId(1L);
    }

    @AfterEach
    private void cleanup (){
        memberRepository.deleteAll();
    }

    @Test
    public void Member_DTO_signup_WithNewEmail() {
        //given
        given(memberSaveRequestDto.toMember(passwordEncoder)).willReturn(savedMember);
        given(memberSaveRequestDto.getEmail()).willReturn(savedMember.getEmail());
        given(memberRepository.existsByEmail(anyString())).willReturn(Boolean.FALSE);
        given(memberRepository.save(savedMember)).willReturn(savedMember);

        //when
        Long memberId = authService.signup(memberSaveRequestDto);

        //then
        Assertions.assertNotNull(memberId);
        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(memberRepository, times(1)).save(isA(Member.class));

    }

    @Test
    public void Member_DTO_signup_WithSameEmail() throws RuntimeException {
        //given
        given(memberSaveRequestDto.getEmail()).willReturn(savedMember.getEmail());
        given(memberRepository.existsByEmail(anyString())).willReturn(Boolean.TRUE);

        //when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signup(memberSaveRequestDto));

        //then
        verify(memberRepository, times(1)).existsByEmail(anyString());
        assertEquals("이미 가입되어 있는 유저입니다.", exception.getMessage());
    }

}