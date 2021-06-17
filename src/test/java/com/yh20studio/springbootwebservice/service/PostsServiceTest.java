package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.posts.PostsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.SessionMemberDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application-jwt.yml" +
        ",classpath:/application-google.yml" +
        ",classpath:/application-postgresqltest.yml"
)
@AutoConfigureMockMvc(addFilters = false)
public class PostsServiceTest {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MemberRepository memberRepository;

    @After("")
    public void cleanup (){
        postsRepository.deleteAll();
    }

    @Test
    @Transactional
    public void Dto데이터가_posts테이블에_저장 (){
        //given
        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
                .orElseThrow(() -> new NoSuchElementException());

        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .member(member)
                .content("테스트a")
                .title("테스트 타이틀a")
                .build();
        //when
        Long savedPostsId = postsService.save(dto);
        //then
        Posts posts = postsRepository.findById(savedPostsId)
                .orElseThrow(() -> new NoSuchElementException());

        Member ownerMember = posts.getMember();

        assertThat(ownerMember.getEmail()).isEqualTo(dto.getMember().getEmail());
        assertThat(posts.getContent()).isEqualTo(dto.getContent());
        assertThat(posts.getTitle()).isEqualTo(dto.getTitle());
    }

}