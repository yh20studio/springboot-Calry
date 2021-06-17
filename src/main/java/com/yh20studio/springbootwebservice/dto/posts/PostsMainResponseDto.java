package com.yh20studio.springbootwebservice.dto.posts;

import com.yh20studio.springbootwebservice.domain.posts.Posts;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class PostsMainResponseDto {

    private Long id;
    private String title;
    private String content;
    private Member member;
    private String modifiedDate;

    public PostsMainResponseDto(Posts entity){
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        member = entity.getMember();
        modifiedDate = toStringDateTime(entity.getModifiedDate());
    }

    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}
