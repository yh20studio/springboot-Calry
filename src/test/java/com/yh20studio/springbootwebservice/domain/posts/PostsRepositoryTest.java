package com.yh20studio.springbootwebservice.domain.posts;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.member.MemberRepository;
import com.yh20studio.springbootwebservice.dto.SessionMemberDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application-jwt.yml" +
        ",classpath:/application-google.yml" +
        ",classpath:/application-postgresqltest.yml"
)
@AutoConfigureMockMvc(addFilters = false)
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    MemberRepository memberRepository;

    @After("")
    public void cleanup() {
        postsRepository.deleteAll();
    }
    
    @Test
    @Transactional
    public void 게시글저장_불러오기(){
        //given
        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
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
    @Transactional
    public void BaseTimeEntity_등록 (){
        //given
        LocalDateTime now = LocalDateTime.now();

        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
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
    @Transactional
    public void Member와Posts관계정의 (){
        //given

        Member member = memberRepository.findByEmail("yh20studio@gmail.com")
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
