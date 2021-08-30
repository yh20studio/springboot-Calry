package com.yh20studio.springbootwebservice.dto.routines;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
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
    private Member member;

    @Builder
    public RoutinesSaveRequestDto(String icon, String title, Integer duration){
        this.icon = icon;
        this.title = title;
        this.duration = duration;
    }

    public Routines toEntity(){

        return Routines.builder()
                .icon(icon)
                .title(title)
                .duration(duration)
                .member(member)
                .build();
    }



}