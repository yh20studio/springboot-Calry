package com.yh20studio.springbootwebservice.dto.todayRoutines;

import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TodayRoutinesSaveListRequestDto {

    private String date;
    private List<TodayRoutines> todayRoutinesList;

    @Builder
    public TodayRoutinesSaveListRequestDto(String date, List<TodayRoutines> todayRoutinesList){
        this.date = date;
        this.todayRoutinesList = todayRoutinesList;
    }
}