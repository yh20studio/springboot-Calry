package com.yh20studio.springbootwebservice.domain.user;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @After("")
    public void cleanup() {
        /**
         이후 테스트 코드에 영향을 끼치지 않기 위해
         테스트 메소드가 끝날때 마다 respository 전체 비우는 코드
         **/
        userRepository.deleteAll();
    }

    @Test
    public void User_add(){
        //given
        userRepository.save(User.builder()
                .name("youngho")
                .email("yh20studio@gmail.com")
                .picture("none")
                .role(User.Role.GUEST)
                .build());

        //when
        List<User> userList = userRepository.findAll();

        //then
        User saved = userList.get(0);
        assertThat(saved.getRole()).isEqualTo(User.Role.GUEST);
    }
}