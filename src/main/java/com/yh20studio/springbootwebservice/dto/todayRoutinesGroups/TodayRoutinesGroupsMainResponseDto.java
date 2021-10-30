package com.yh20studio.springbootwebservice.dto.todayRoutinesGroups;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TodayRoutinesGroupsMainResponseDto {

    private Long id;
    private LocalDate date;
    private Integer success;
    private Integer fail;
    private List<TodayRoutines> todayRoutinesList = new ArrayList<>();
    private Member member;


    // todayRoutines 중에서 완료되지 않은 값만 내보낸다.
    public TodayRoutinesGroupsMainResponseDto(TodayRoutinesGroups entity){
        id = entity.getId();
        date = entity.getDate();
        success = entity.getSuccess();
        fail = entity.getFail();
        member = entity.getMember();
        if(entity.getTodayRoutinesList() != null){
            entity.getTodayRoutinesList().forEach(todayRoutines -> {
                if(todayRoutines.getFinish() == Boolean.FALSE){
                    todayRoutinesList.add(todayRoutines);
                }
            });
        }

    }

}