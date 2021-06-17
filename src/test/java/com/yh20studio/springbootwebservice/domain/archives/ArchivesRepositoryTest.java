package com.yh20studio.springbootwebservice.domain.archives;

import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.archives.ArchivesRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.dto.SessionMemberDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSaveRequestDto;
import com.yh20studio.springbootwebservice.service.MemberService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application-jwt.yml" +
        ",classpath:/application-google.yml" +
        ",classpath:/application-postgresqltest.yml"
)
@AutoConfigureMockMvc(addFilters = false)
public class ArchivesRepositoryTest {

    @Autowired
    ArchivesRepository archivesRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @After("")
    public void cleanup() {
        /**
         이후 테스트 코드에 영향을 끼치지 않기 위해
         테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
         **/
        archivesRepository.deleteAll();
    }

    @Test
    @Transactional
    public void Archives_저장_불러오기(){
        //given
        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
                .orElseThrow(() -> new NoSuchElementException());

        archivesRepository.save(Archives.builder()
                .title("Archives_저장_불러오기")
                .content("Archives_저장_불러오기 본문")
                .url("Archives_저장_불러오기 Url")
                .member(member)
                .build());
        //when
        List<Archives> archivesList = archivesRepository.findAll();

        //then
        Archives archives = archivesList.get(0);
        assertThat(archives.getTitle()).isEqualTo("Archives_저장_불러오기");
        assertThat(archives.getContent()).isEqualTo("Archives_저장_불러오기 본문");
        assertThat(archives.getUrl()).isEqualTo("Archives_저장_불러오기 Url");
    }

    @Test
    @Transactional
    public void BaseTimeEntity_등록 (){
        //given
        LocalDateTime now = LocalDateTime.now();

        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
                .orElseThrow(() -> new NoSuchElementException());

        archivesRepository.save(Archives.builder()
                .title("BaseTimeEntity_등록")
                .content("BaseTimeEntity_등록 본문")
                .url("BaseTimeEntity_등록 Url")
                .member(member)
                .build());

        //when
        List<Archives> archivesList = archivesRepository.findAll();

        //then
        Archives archives = archivesList.get(0);
        assertThat(archives.getCreatedDate()).isAfter(now);
        assertThat(archives.getModifiedDate()).isAfter(now);
    }
    @Test
    @Transactional
    public void Member_Archives_관계정의 (){
        //given
        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
                .orElseThrow(() -> new NoSuchElementException());
        //when
        Archives savedArchives = archivesRepository.save(Archives.builder()
                .title("Member_Archives_관계정의")
                .content("Member_Archives_관계정의 본문")
                .url("Member_Archives_관계정의 Url")
                .member(member)
                .build());
        //then

        assertThat(savedArchives.getTitle()).isEqualTo("Member_Archives_관계정의");
        assertThat(savedArchives.getContent()).isEqualTo("Member_Archives_관계정의 본문");
        assertThat(savedArchives.getUrl()).isEqualTo("Member_Archives_관계정의 Url");
        assertThat(savedArchives.getMember()).isEqualTo(member);

    }
}
