package com.yh20studio.springbootwebservice.dto.member;

import com.yh20studio.springbootwebservice.domain.member.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application-jwt.yml" +
        ",classpath:/application-postgresqltest.yml"
)
class MemberSaveRequestDtoTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void MemberSaveRequestDto_builder(){
        //given
        String email = "test@naver.com";
        String name = "test";
        String picture = "picture";
        String resource = "resource";
        String password = "password";
        Member.Role role = Member.Role.GUEST;
        //when
        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .resource(resource)
                .password(password)
                .role(role)
                .build();

        //then
        assertThat(dto.getEmail()).isEqualTo(email);
        assertThat(dto.getName()).isEqualTo(name); //assertThat: 테스트 검증 라이브러리
        assertThat(dto.getPicture()).isEqualTo(picture);
        assertThat(dto.getResource()).isEqualTo(resource);
        assertThat(dto.getPassword()).isEqualTo(password);
        assertThat(dto.getRole()).isEqualTo(role);
    }
    @Test
    public void MemberSaveRequestDto_toEntity(){
        //given
        String email = "test@naver.com";
        String name = "test";
        String picture = "picture";
        String resource = "resource";
        Member.Role role = Member.Role.GUEST;

        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .resource(resource)
                .role(role)
                .build();
        //when
        Member member = dto.toEntity();

        //then
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getName()).isEqualTo(name); //assertThat: 테스트 검증 라이브러리
        assertThat(member.getPicture()).isEqualTo(picture);
        assertThat(member.getResource()).isEqualTo(resource);
        assertThat(member.getRole()).isEqualTo(role);
    }

    @Test
    public void MemberSaveRequestDto_toMember(){
        //given
        String email = "test@naver.com";
        String name = "test";
        String picture = "picture";
        String resource = "resource";
        String password = "password";
        Member.Role role = Member.Role.GUEST;

        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .resource(resource)
                .password(password)
                .role(role)
                .build();
        //when

        Member member = dto.toMember(passwordEncoder);

        //then
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getName()).isEqualTo(name); //assertThat: 테스트 검증 라이브러리
        assertThat(member.getPicture()).isEqualTo(picture);
        assertThat(member.getResource()).isEqualTo(resource);
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
        assertThat(member.getRole()).isEqualTo(role);
    }

    @Test
    public void MemberSaveRequestDto_toAuthentication(){
        //given
        String email = "test@naver.com";
        String name = "test";
        String picture = "picture";
        String resource = "resource";
        String password = "password";
        Member.Role role = Member.Role.GUEST;

        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .resource(resource)
                .password(password)
                .role(role)
                .build();
        //when

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = dto.toAuthentication();

        //then
        Assertions.assertNotNull(usernamePasswordAuthenticationToken);
    }

}