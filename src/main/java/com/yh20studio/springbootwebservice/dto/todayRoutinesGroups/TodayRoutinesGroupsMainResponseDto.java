package com.yh20studio.springbootwebservice.dto.todayRoutinesGroups;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class TodayRoutinesGroupsMainResponseDto {

    private Long id;
    private LocalDate date;
    private Integer grade;
    private List<TodayRoutines> todayRoutinesList;
    private Member member;


    public TodayRoutinesGroupsMainResponseDto(TodayRoutinesGroups entity){
        id = entity.getId();
        date = entity.getDate();
        grade = entity.getGrade();
        todayRoutinesList = entity.getTodayRoutinesList();
        member = entity.getMember();
    }

}