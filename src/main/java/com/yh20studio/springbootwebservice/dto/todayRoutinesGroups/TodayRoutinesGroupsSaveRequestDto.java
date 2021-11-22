package com.yh20studio.springbootwebservice.dto.todayRoutinesGroups;

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
public class TodayRoutinesGroupsSaveRequestDto {

    private LocalDate date;
    private Integer success;
    private Integer fail;
    private Member member;


    @Builder
    public TodayRoutinesGroupsSaveRequestDto(String date, Integer success, Integer fail,
        Member member) {
        this.date = LocalDate.parse(date);
        this.success = success;
        this.fail = fail;
        this.member = member;
    }

    public TodayRoutinesGroups toEntity() {
        return TodayRoutinesGroups.builder()
            .date(date)
            .success(success)
            .fail(fail)
            .member(member)
            .build();
    }

}