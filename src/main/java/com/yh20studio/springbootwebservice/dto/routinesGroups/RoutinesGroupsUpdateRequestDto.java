package com.yh20studio.springbootwebservice.dto.routinesGroups;

import com.yh20studio.springbootwebservice.domain.routines.Routines;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoutinesGroupsUpdateRequestDto {

    private Long id;
    private Routines routines;
    private RoutinesGroupsUnions routinesGroupsUnions;

    @Builder
    public RoutinesGroupsUpdateRequestDto(Long id, Routines routines){
        this.id = id;
        this.routines = routines;
    }

    public RoutinesGroups toEntity(){
        return RoutinesGroups.builder()
                .routines(routines)
                .routinesGroupsUnions(routinesGroupsUnions)
                .build();
    }

}