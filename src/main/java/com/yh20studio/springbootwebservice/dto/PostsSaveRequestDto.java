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
    private User owner;

    @Builder
    public PostsSaveRequestDto(String title, String content, User owner){
        this.title = title;
        this.content = content;
        this.owner = owner;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .owner(owner)
                .build();
    }
}

