package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @After("")
    public void cleanup (){
        memberRepository.deleteAll();
    }

    @Test
    public void Member_DTO_데이터저장 (){
        //given
        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .resource("google")
                .role(Member.Role.GUEST)
                .build();

        //when
        memberService.save(dto);

        //then
        Member member = memberRepository.findAll().get(0);
        assertThat(member.getName()).isEqualTo(dto.getName());
        assertThat(member.getEmail()).isEqualTo(dto.getEmail());
        assertThat(member.getPicture()).isEqualTo(dto.getPicture());
    }

    @Test
    public void Member_DTO_데이터저장및업데이트 (){
        //given
        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .resource("google")
                .role(Member.Role.GUEST)
                .build();

        //when
        memberService.save(dto);

        //then
        Member member = memberRepository.findAll().get(0);
        assertThat(member.getName()).isEqualTo(dto.getName());
        assertThat(member.getEmail()).isEqualTo(dto.getEmail());
        assertThat(member.getPicture()).isEqualTo(dto.getPicture());
    }
}
