package com.yh20studio.springbootwebservice.dto;

import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArchivesSaveRequestDto {

    private String title;
    private String content;
    private String url;
    private String author;

    @Builder
    public ArchivesSaveRequestDto(String title, String content, String url, String author){
        this.title = title;
        this.content = content;
        this.url = url;
        this.author = author;
    }

    public Archives toEntity(){
        return Archives.builder()
                .title(title)
                .content(content)
                .url(url)
                .author(author)
                .build();
    }
}