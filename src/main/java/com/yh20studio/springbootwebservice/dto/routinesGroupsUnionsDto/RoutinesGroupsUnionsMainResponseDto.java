package com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto;

import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsMainResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Getter
public class RoutinesGroupsUnionsMainResponseDto {

    private Long id;
    private String title;
    private List<RoutinesGroups> routinesGroupsList;

    public RoutinesGroupsUnionsMainResponseDto(RoutinesGroupsUnions entity){
        id = entity.getId();
        title = entity.getTitle();
        routinesGroupsList = entity.getRoutinesGroupsList();
    }


}