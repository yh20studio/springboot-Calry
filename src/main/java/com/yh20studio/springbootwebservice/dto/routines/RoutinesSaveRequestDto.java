package com.yh20studio.springbootwebservice.dto.routines;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RoutinesSaveRequestDto {

    private String icon;
    private String title;
    private Integer duration;
    private RoutinesGroups routines_groups;

    @Builder
    public RoutinesSaveRequestDto(String icon, String title, Integer duration, RoutinesGroups routines_groups){
        this.icon = icon;
        this.title = title;
        this.duration = duration;
        this.routines_groups = routines_groups;
    }

    public Routines toEntity(){

        return Routines.builder()
                .icon(icon)
                .title(title)
                .duration(duration)
                .routines_groups(routines_groups)
                .build();
    }


}