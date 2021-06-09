package com.yh20studio.springbootwebservice.domain;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.SessionMemberDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

// @ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    MemberRepository memberRepository;

    @After("")
    public void cleanup() {
        /**
         이후 테스트 코드에 영향을 끼치지 않기 위해
         테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
         **/
        postsRepository.deleteAll();
    }
    
    @Test
    public void 게시글저장_불러오기(){
        //given
        SessionMemberDto memberDto = SessionMemberDto.builder()
                .name("2young")
                .email("tym9169@gmail.com")
                .picture("none")
                .build();

        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException());

        postsRepository.save(Posts.builder()
                .title("테스트 게시글입니다.")
                .content("테스트 본문")
                .member(member)
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo("테스트 게시글입니다.");
        assertThat(posts.getContent()).isEqualTo("테스트 본문");
    }

    @Test
    public void BaseTimeEntity_등록 (){
        //given
        LocalDateTime now = LocalDateTime.now();

        SessionMemberDto memberDto = SessionMemberDto.builder()
                .name("2young")
                .email("tym9169@gmail.com")
                .picture("none")
                .build();

        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException());

        postsRepository.save(Posts.builder()
                .title("테스트 게시글입니다.")
                .content("테스트 본문")
                .member(member)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
    @Test
    public void Member와Posts관계정의 (){
        //given

        Member member = memberRepository.findByEmail("tym9169@gmail.com")
                .orElseThrow(() -> new NoSuchElementException());
        //when
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("Member와Posts관계정의")
                .content("Member와Posts관계정의 본문")
                .member(member)
                .build());
        //then

        assertThat(savedPosts.getTitle()).isEqualTo("Member와Posts관계정의");
        assertThat(savedPosts.getContent()).isEqualTo("Member와Posts관계정의 본문");
        assertThat(savedPosts.getMember()).isEqualTo(member);

    }
}
