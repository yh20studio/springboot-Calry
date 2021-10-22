package com.yh20studio.springbootwebservice.dto.quickSchedules;

import com.yh20studio.springbootwebservice.domain.quickSchedules.QuickSchedules;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.dto.labels.LabelsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSummaryDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;

@Getter
public class QuickSchedulesMainResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalTime start_time;
    private LocalTime end_time;
    private LabelsMainResponseDto labels;
    private MemberSummaryDto member;


    public QuickSchedulesMainResponseDto(QuickSchedules entity){
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        start_time = entity.getStart_time();
        end_time = entity.getEnd_time();
        labels = new LabelsMainResponseDto(entity.getLabels());
        member = new MemberSummaryDto(entity.getMember());
    }

}