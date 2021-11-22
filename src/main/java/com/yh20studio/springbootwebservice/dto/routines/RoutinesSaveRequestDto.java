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

    private String title;
    private Integer duration;
    private Member member;

    @Builder
    public RoutinesSaveRequestDto(String title, Integer duration) {
        this.title = title;
        this.duration = duration;
    }

    public Routines toEntity() {

        return Routines.builder()
            .title(title)
            .duration(duration)
            .member(member)
            .build();
    }
}