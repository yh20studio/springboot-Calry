package com.yh20studio.springbootwebservice.dto.routinesGroups;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoutinesGroupsSaveRequestDto {

    private Long routines_id;
    private String title;
    private Routines routines;
    private Member member;

    @Builder
    public RoutinesGroupsSaveRequestDto(String title, Routines routines){
        this.title = title;
        this.routines = routines;
    }

    public RoutinesGroups toEntity(){
        return RoutinesGroups.builder()
                .title(title)
                .routines(routines)
                .member(member)
                .build();
    }

    public void setMember(Member member){
        this.member = member;
    }



}