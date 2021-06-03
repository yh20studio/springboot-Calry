package com.yh20studio.springbootwebservice.dto;

import com.yh20studio.springbootwebservice.domain.archives.Archives;
import com.yh20studio.springbootwebservice.domain.user.User;
import lombok.Getter;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class ArchivesMainResponseDto {

    private Long id;
    private String title;
    private String content;
    private String url;
    private String author;
    private String modifiedDate;

    public ArchivesMainResponseDto(Archives entity){
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        url = entity.getUrl();
        author = entity.getAuthor();
        modifiedDate = toStringDateTime(entity.getModifiedDate());
    }

    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}
