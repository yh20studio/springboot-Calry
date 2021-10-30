package com.yh20studio.springbootwebservice.dto.todayRoutines;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class TodayRoutinesMainResponseDto {

    private Long id;
    private LocalTime finishTime;
    private Boolean finish;
    private Routines routines;


    public TodayRoutinesMainResponseDto(TodayRoutines entity){
        id = entity.getId();
        finishTime = entity.getFinishTime();
        finish = entity.getFinish();
        routines = entity.getRoutines();
    }

}