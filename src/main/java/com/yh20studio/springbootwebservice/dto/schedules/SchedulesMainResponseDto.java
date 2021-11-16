package com.yh20studio.springbootwebservice.dto.schedules;

import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.schedules.Schedules;
import com.yh20studio.springbootwebservice.dto.labels.LabelsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.member.MemberMainResponseDto;
import com.yh20studio.springbootwebservice.dto.member.MemberSummaryDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;

@Getter
public class SchedulesMainResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Labels labels;


    public SchedulesMainResponseDto(Schedules entity){
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        start_date = entity.getStart_date();
        end_date = entity.getEnd_date();
        labels = entity.getLabels();
    }

}





