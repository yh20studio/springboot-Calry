package com.yh20studio.springbootwebservice.dto.routines;

import com.yh20studio.springbootwebservice.domain.routines.Routines_memos;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class MemosMainResponseDto {

    private Long id;
    private Long routines_id;
    private String content;
    private Routines routines;
    private LocalDateTime modified_date;
    private LocalDateTime created_date;

    public MemosMainResponseDto(Routines_memos entity){
        id = entity.getId();
        routines_id = entity.getRoutines().getId();
        content = entity.getContent();
        routines = entity.getRoutines();
        modified_date = entity.getModified_date();
        created_date = entity.getCreated_date();
    }

    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}