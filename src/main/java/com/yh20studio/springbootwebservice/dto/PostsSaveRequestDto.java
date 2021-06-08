package com.yh20studio.springbootwebservice.dto;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String content;
    private User user;

    @Builder
    public PostsSaveRequestDto(String title, String content, User user){
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}

