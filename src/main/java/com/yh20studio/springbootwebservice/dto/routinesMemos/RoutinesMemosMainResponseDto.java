package com.yh20studio.springbootwebservice.dto.routinesMemos;

import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class RoutinesMemosMainResponseDto {

    private Long id;
    private String content;
    private LocalDateTime modified_date;
    private LocalDateTime created_date;

    public RoutinesMemosMainResponseDto(RoutinesMemos entity){
        id = entity.getId();
        content = entity.getContent();
        modified_date = entity.getModified_date();
        created_date = entity.getCreated_date();
    }

}