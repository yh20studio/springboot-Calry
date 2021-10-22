package com.yh20studio.springbootwebservice.dto.routinesGroups;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;

import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import lombok.Getter;

import java.util.List;

@Getter
public class RoutinesGroupsMainResponseDto {

    private Long id;
    private String title;
    private List<Routines> routines_List;

    public RoutinesGroupsMainResponseDto(RoutinesGroups entity){
        id = entity.getId();
        title = entity.getTitle();
        routines_List = entity.getRoutinesList();

    }
}