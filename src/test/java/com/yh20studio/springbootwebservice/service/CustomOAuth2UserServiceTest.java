package com.yh20studio.springbootwebservice.service;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.posts.PostsRepository;
import com.yh20studio.springbootwebservice.domain.user.User;
import com.yh20studio.springbootwebservice.domain.user.UserRepository;
import com.yh20studio.springbootwebservice.dto.PostsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.UserSaveRequestDto;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomOAuth2UserServiceTest {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private UserRepository userRepository;

    @After("")
    public void cleanup (){
        userRepository.deleteAll();
    }

    @Test
    public void User_DTO_데이터저장 (){
        //given
        UserSaveRequestDto dto = UserSaveRequestDto.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .role(User.Role.GUEST)
                .build();

        //when
        customOAuth2UserService.save(dto);

        //then
        User user = userRepository.findAll().get(0);
        assertThat(user.getName()).isEqualTo(dto.getName());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getPicture()).isEqualTo(dto.getPicture());
    }

    @Test
    public void User_DTO_데이터저장및업데이트 (){
        //given
        UserSaveRequestDto dto = UserSaveRequestDto.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .role(User.Role.GUEST)
                .build();

        //when
        customOAuth2UserService.save(dto);

        //then
        User user = userRepository.findAll().get(0);
        assertThat(user.getName()).isEqualTo(dto.getName());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getPicture()).isEqualTo(dto.getPicture());
    }

}