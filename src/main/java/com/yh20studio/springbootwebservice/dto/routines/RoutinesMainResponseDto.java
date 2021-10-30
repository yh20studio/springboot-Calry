package com.yh20studio.springbootwebservice.dto.routines;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Getter
public class RoutinesMainResponseDto {

    private Long id;
    private String title;
    private List<RoutinesMemos> routines_memosList;
    private Integer duration;

    public RoutinesMainResponseDto(Routines entity){
        id = entity.getId();
        title = entity.getTitle();
        routines_memosList = entity.getRoutines_memosList();
        duration = entity.getDuration();
    }

}