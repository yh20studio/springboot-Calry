package com.yh20studio.springbootwebservice.domain;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.domain.user.User;
import com.yh20studio.springbootwebservice.domain.user.UserRepository;
import com.yh20studio.springbootwebservice.dto.SessionUserDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Session;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

// @ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UserRepository userRepository;

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
        SessionUserDto userDto = SessionUserDto.builder()
                .name("2young")
                .email("tym9169@gmail.com")
                .picture("none")
                .build();

        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException());

        postsRepository.save(Posts.builder()
                .title("테스트 게시글입니다.")
                .content("테스트 본문")
                .owner(user)
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

        SessionUserDto userDto = SessionUserDto.builder()
                .name("2young")
                .email("tym9169@gmail.com")
                .picture("none")
                .build();

        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException());

        postsRepository.save(Posts.builder()
                .title("테스트 게시글입니다.")
                .content("테스트 본문")
                .owner(user)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
