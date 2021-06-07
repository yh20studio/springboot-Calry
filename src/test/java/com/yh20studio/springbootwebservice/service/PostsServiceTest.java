package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.domain.user.User;
import com.yh20studio.springbootwebservice.domain.user.UserRepository;
import com.yh20studio.springbootwebservice.dto.PostsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.SessionUserDto;
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
    private UserRepository userRepository;

    @After("Dto데이터가_posts테이블에_저장")
    public void cleanup (){
        postsRepository.deleteAll();
    }

    @Test
    public void Dto데이터가_posts테이블에_저장 (){
        //given
        SessionUserDto userDto = SessionUserDto.builder()
                .name("2young")
                .email("tym9169@gmail.com")
                .picture("none")
                .build();

        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException());

        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .owner(user)
                .content("테스트")
                .title("테스트 타이틀")
                .build();

        //when
        postsService.save(dto);

        //then
        Posts posts = postsRepository.findAll().get(0);
        User owner = posts.getOwner();
        System.out.println(owner.getEmail());
        System.out.println(owner);

        assertThat(posts.getOwner()).isEqualTo(dto.getOwner());
        assertThat(posts.getContent()).isEqualTo(dto.getContent());
        assertThat(posts.getTitle()).isEqualTo(dto.getTitle());
    }

}