package com.yh20studio.springbootwebservice.dto.routines;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutines;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class CustomRoutinesMainResponseDto {

    private Long id;
    private String icon;
    private String title;
    private Integer duration;
    private Member member;
    private String modifiedDate;

    public CustomRoutinesMainResponseDto(CustomRoutines entity){
        id = entity.getId();
        icon = entity.getIcon();
        title = entity.getTitle();
        duration = entity.getDuration();
        member = entity.getMember();
        modifiedDate = toStringDateTime(entity.getModified_date());
    }

    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}