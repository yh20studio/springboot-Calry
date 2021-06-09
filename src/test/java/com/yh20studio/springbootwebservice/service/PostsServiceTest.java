package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.PostsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.SessionMemberDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
    public void Dto데이터가_posts테이블에_저장 (){
        //given
        SessionMemberDto memberDto = SessionMemberDto.builder()
                .name("2young")
                .email("tym9169@gmail.com")
                .picture("none")
                .build();

        Member member = memberRepository.findByEmail(memberDto.getEmail())
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