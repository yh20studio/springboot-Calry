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
    private Integer grade;
    private Member member;

    @Builder
    public TodayRoutinesGroupsSaveRequestDto(String date, Integer grade, Member member){
        this.date = LocalDate.parse(date);;
        this.grade = grade;
        this.member = member;
    }

    public TodayRoutinesGroups toEntity(){
        return TodayRoutinesGroups.builder()
                .date(date)
                .grade(grade)
                .member(member)
                .build();
    }

}