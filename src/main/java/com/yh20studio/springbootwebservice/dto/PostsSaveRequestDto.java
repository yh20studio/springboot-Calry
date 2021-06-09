package com.yh20studio.springbootwebservice.dto;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.member.Member;
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
    private Member member;

    @Builder
    public PostsSaveRequestDto(String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public Posts toEntity(){
        return Posts.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}

