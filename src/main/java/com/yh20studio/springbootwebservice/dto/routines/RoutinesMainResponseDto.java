package com.yh20studio.springbootwebservice.dto.routines;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routines.Routines_memos;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Getter
public class RoutinesMainResponseDto {

    private Long id;
    private String icon;
    private String title;
    private List<Routines_memos> routines_memosList;
    private Integer duration;
    private Member member;
    private String modifiedDate;

    public RoutinesMainResponseDto(Routines entity){
        id = entity.getId();
        icon = entity.getIcon();
        title = entity.getTitle();
        routines_memosList = entity.getRoutines_memosList();
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