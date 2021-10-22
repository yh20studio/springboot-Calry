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
    private LabelsMainResponseDto labels;
    private MemberSummaryDto member;


    public SchedulesMainResponseDto(Schedules entity){
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        start_date = entity.getStart_date();
        end_date = entity.getEnd_date();
        labels = new LabelsMainResponseDto(entity.getLabels());
        member = new MemberSummaryDto(entity.getLabels().getMember());
    }

    // 일정의 시작날짜의 따라서 오름차순으로 정리하는 Comparator
    public static class SchedulesMainResponseDtoDateTimeComparator implements Comparator<SchedulesMainResponseDto> {
        @Override
        public int compare(SchedulesMainResponseDto s1, SchedulesMainResponseDto s2) {
            if (s1.getStart_date().isBefore(s2.getStart_date())) {
                return 1;
            } else if (s1.getStart_date().isAfter(s2.getStart_date())) {
                return -1;
            }
            return 0;
        }
    }


}





