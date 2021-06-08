package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.domain.user.User;
import com.yh20studio.springbootwebservice.domain.user.UserRepository;
import com.yh20studio.springbootwebservice.dto.PostsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.PostsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.SessionUserDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostsServiceTest {

    @Autowired
    private PostsService postsService;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserRepository userRepository;

    @After("")
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
                .user(user)
                .content("테스트a")
                .title("테스트 타이틀a")
                .build();
        //when
        Long savedPostsId = postsService.save(dto);
        //then
        Posts posts = postsRepository.findById(savedPostsId)
                .orElseThrow(() -> new NoSuchElementException());

        User ownerUser = posts.getUser();

        assertThat(ownerUser.getEmail()).isEqualTo(dto.getUser().getEmail());
        assertThat(posts.getContent()).isEqualTo(dto.getContent());
        assertThat(posts.getTitle()).isEqualTo(dto.getTitle());
    }

}