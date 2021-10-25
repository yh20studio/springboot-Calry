package com.yh20studio.springbootwebservice.dto.todayRoutines;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class TodayRoutinesSaveRequestDto {

    private LocalTime finishTime;
    private Boolean finish;
    private String date;
    private TodayRoutinesGroups todayRoutinesGroups;
    private Routines routines;

    @Builder
    public TodayRoutinesSaveRequestDto(String finishTime, Boolean finish, String date, Routines routines){
        this.finishTime = LocalTime.parse(finishTime);;
        this.finish = finish;
        this.date = date;
        this.routines = routines;
    }

    public TodayRoutines toEntity(){

        return TodayRoutines.builder()
                .finishTime(finishTime)
                .finish(finish)
                .todayRoutinesGroups(todayRoutinesGroups)
                .routines(routines)
                .build();
    }
}