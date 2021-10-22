package com.yh20studio.springbootwebservice.dto.routinesMemos;

import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
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
    private String modified_date;
    private String created_date;

    public MemosMainResponseDto(RoutinesMemos entity){
        id = entity.getId();
        routines_id = entity.getRoutines().getId();
        content = entity.getContent();
        routines = entity.getRoutines();
        modified_date = toStringDateTime(entity.getModified_date());
        created_date = toStringDateTime(entity.getCreated_date());
    }

    // RoutinesMemos가 수정 및 생성된 LocalDateTime을 Json으로 내보내기 위하여 String으로 변환
    private String toStringDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}