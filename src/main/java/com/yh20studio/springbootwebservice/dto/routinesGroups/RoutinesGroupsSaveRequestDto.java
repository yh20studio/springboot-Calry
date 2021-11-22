package com.yh20studio.springbootwebservice.dto.routinesGroups;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import com.yh20studio.springbootwebservice.domain.routinesMemos.RoutinesMemos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoutinesGroupsSaveRequestDto {

    private Routines routines;
    private RoutinesGroupsUnions routinesGroupsUnions;

    @Builder
    public RoutinesGroupsSaveRequestDto(Routines routines) {
        this.routines = routines;
    }

    public RoutinesGroups toEntity() {
        return RoutinesGroups.builder()
            .routines(routines)
            .routinesGroupsUnions(routinesGroupsUnions)
            .build();
    }
}