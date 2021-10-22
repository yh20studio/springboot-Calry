package com.yh20studio.springbootwebservice.dto.schedules;

import com.yh20studio.springbootwebservice.domain.labelColors.LabelColors;
import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class SchedulesSaveRequestDto {

    private String title;
    private String content;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Labels labels;
    private Member member;

    @Builder
    public SchedulesSaveRequestDto(String title, String content, String start_date, String end_date, Labels labels, Member member){
        this.title = title;
        this.content = content;
        this.start_date = stringToLocalDateTime(start_date);
        this.end_date = stringToLocalDateTime(end_date);
        this.labels = labels;
        this.member = member;
    }

    public Schedules toEntity(){
        return Schedules.builder()
                .title(title)
                .content(content)
                .start_date(start_date)
                .end_date(end_date)
                .labels(labels)
                .member(member)
                .build();
    }

    // json으로 받아온 날짜에 대한 String 값을 LocalDateTime으로 변환
    public LocalDateTime stringToLocalDateTime(String str){
        return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME)   ;
    }

    public void setMember(Member member){
        this.member = member;
    }



}