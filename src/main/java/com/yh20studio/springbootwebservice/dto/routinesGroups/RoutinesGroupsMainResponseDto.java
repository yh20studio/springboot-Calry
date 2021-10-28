package com.yh20studio.springbootwebservice.dto.routinesGroups;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;

import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class RoutinesGroupsMainResponseDto {

    private Long id;
    private Routines routines;
    private RoutinesGroupsUnions routinesGroupsUnions;

    public RoutinesGroupsMainResponseDto(RoutinesGroups entity){
        id = entity.getId();
        routines = entity.getRoutines();
        routinesGroupsUnions = entity.getRoutinesGroupsUnions();
    }

}
