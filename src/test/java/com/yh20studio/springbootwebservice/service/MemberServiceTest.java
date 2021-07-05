package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.component.SecurityUtil;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.OAuthAttributes;
import com.yh20studio.springbootwebservice.dto.member.MemberMainResponseDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application-jwt.yml" +
        ",classpath:/application-google.yml" +
        ",classpath:/application-postgresqltest.yml"
)
@AutoConfigureMockMvc(addFilters = false)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @After("")
    public void cleanup (){
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    public void Member_DTO_save (){
        //given
        MemberSaveRequestDto dto = MemberSaveRequestDto.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .resource("google")
                .password(passwordEncoder.encode("123"))
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
    @Transactional
    public void OAuthAttributes_DTO_saveOrUpdate (){
        //given
        OAuthAttributes attributes = OAuthAttributes.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .resource("google")
                .build();

        //when
        memberService.saveOrUpdate(attributes);

        //then
        Member member = memberRepository.findAll().get(0);
        assertThat(member.getName()).isEqualTo(attributes.getName());
        assertThat(member.getEmail()).isEqualTo(attributes.getEmail());
        assertThat(member.getPicture()).isEqualTo(attributes.getPicture());
    }

    @Test
    public void OAuthAttributes_DTO_getMyInfo () {
        //given
        OAuthAttributes attributes = OAuthAttributes.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .resource("google")
                .build();

        //when
        securityUtil.getCurrentMemberId();
        MemberMainResponseDto myInfo = memberService.getMyInfo();

        //then
        assertThat(myInfo.getName()).isEqualTo(attributes.getName());
        assertThat(myInfo.getEmail()).isEqualTo(attributes.getEmail());
        assertThat(myInfo.getPicture()).isEqualTo(attributes.getPicture());
    }

}
