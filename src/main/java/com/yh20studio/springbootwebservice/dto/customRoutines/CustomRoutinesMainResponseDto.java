package com.yh20studio.springbootwebservice.dto.customRoutines;

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


    public CustomRoutinesMainResponseDto(CustomRoutines entity){
        id = entity.getId();
        icon = entity.getIcon();
        title = entity.getTitle();
        duration = entity.getDuration();
        member = entity.getMember();

    }

}