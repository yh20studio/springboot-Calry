package com.yh20studio.springbootwebservice.dto.quickSchedules;

import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.quickSchedules.QuickSchedules;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class QuickSchedulesSaveRequestDto {

    private String title;
    private String content;
    private LocalTime start_time;
    private LocalTime end_time;
    private Labels labels;
    private Member member;

    @Builder
    public QuickSchedulesSaveRequestDto(String title, String content, String start_time, String end_time, Labels labels, Member member){
        this.title = title;
        this.content = content;
        this.start_time = LocalTime.parse(start_time);
        this.end_time = LocalTime.parse(end_time);
        this.labels = labels;
        this.member = member;
    }

    public QuickSchedules toEntity(){
        return QuickSchedules.builder()
                .title(title)
                .content(content)
                .start_time(start_time)
                .end_time(end_time)
                .labels(labels)
                .member(member)
                .build();
    }

    public void setMember(Member member){
        this.member = member;
    }

}