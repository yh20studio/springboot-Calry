package com.yh20studio.springbootwebservice.dto;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSaveRequestDto {

    private String name;
    private String email;
    private String picture;
    private User.Role role;

    @Builder
    public UserSaveRequestDto(String name, String email, String picture, User.Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(User.Role.GUEST)
                .build();
    }
}
