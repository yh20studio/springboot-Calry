package com.yh20studio.springbootwebservice.dto.todayRoutines;

import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.todayRoutines.TodayRoutines;
import com.yh20studio.springbootwebservice.domain.todayRoutinesGroups.TodayRoutinesGroups;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
public class TodayRoutinesUpdateRequestDto {

    private LocalTime finishTime;
    private Boolean finish;

    private TodayRoutinesGroups todayRoutinesGroups;
    private Routines routines;

    @Builder
    public TodayRoutinesUpdateRequestDto(String finishTime, Boolean finish) {
        this.finishTime = LocalTime.parse(finishTime);
        ;
        this.finish = finish;
    }

    public TodayRoutines toEntity() {

        return TodayRoutines.builder()
            .finishTime(finishTime)
            .finish(finish)
            .todayRoutinesGroups(todayRoutinesGroups)
            .routines(routines)
            .build();
    }
}