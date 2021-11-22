package com.yh20studio.springbootwebservice.dto.routinesMemos;

import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoutinesMemosSaveRequestDto {

    private Long routines_id;
    private String content;
    private Routines routines;

    @Builder
    public RoutinesMemosSaveRequestDto(Long routines_id, String content) {
        this.routines_id = routines_id;
        this.content = content;
    }

    public RoutinesMemos toEntity() {
        return RoutinesMemos.builder()
            .content(content)
            .routines(routines)
            .build();
    }
}