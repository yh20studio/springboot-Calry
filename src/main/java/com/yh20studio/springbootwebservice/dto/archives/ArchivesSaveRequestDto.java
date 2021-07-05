package com.yh20studio.springbootwebservice.dto.archives;

import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
@NoArgsConstructor
public class ArchivesSaveRequestDto {

    private String title;
    private String content;
    private String url;
    private Member member;

    @Builder
    public ArchivesSaveRequestDto(String title, String content, String url){
        this.title = title;
        this.content = content;
        this.url = url;
    }

    public Archives toEntity(){

        return Archives.builder()
                .title(title)
                .content(content)
                .url(url)
                .member(member)
                .build();
    }



}