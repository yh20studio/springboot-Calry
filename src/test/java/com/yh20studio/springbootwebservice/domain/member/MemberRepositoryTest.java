package com.yh20studio.springbootwebservice.domain.member;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application-jwt.yml" +
        ",classpath:/application-google.yml" +
        ",classpath:/application-postgresqltest.yml"
)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @After("")
    public void cleanup() {
        /**
         이후 테스트 코드에 영향을 끼치지 않기 위해
         테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
         **/
        memberRepository.deleteAll();
    }

    @Test
    public void Member_add(){
        //given
        memberRepository.save(Member.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .role(Member.Role.GUEST)
                .build());

        //when
        List<Member> memberList = memberRepository.findAll();

        //then
        Member saved = memberList.get(0);
        assertThat(saved.getRole()).isEqualTo(Member.Role.GUEST);
    }
}