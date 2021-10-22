package com.yh20studio.springbootwebservice.dto.customRoutines;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.customRoutines.CustomRoutines;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomRoutinesSaveRequestDto {

    private String icon;
    private String title;
    private Integer duration;
    private Member member;

    @Builder
    public CustomRoutinesSaveRequestDto(String icon, String title, Integer duration){
        this.icon = icon;
        this.title = title;
        this.duration = duration;
    }

    public CustomRoutines toEntity(){

        return CustomRoutines.builder()
                .icon(icon)
                .title(title)
                .duration(duration)
                .member(member)
                .build();
    }



}