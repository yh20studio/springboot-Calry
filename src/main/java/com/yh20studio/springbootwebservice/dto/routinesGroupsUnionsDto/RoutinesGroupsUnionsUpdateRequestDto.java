package com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoutinesGroupsUnionsUpdateRequestDto {

    private String title;
    private List<RoutinesGroupsUpdateRequestDto> routinesGroupsList;
    private Member member;

    @Builder
    public RoutinesGroupsUnionsUpdateRequestDto(String title,
        List<RoutinesGroupsUpdateRequestDto> routinesGroupsList) {
        this.title = title;
        this.routinesGroupsList = routinesGroupsList;
    }

    public RoutinesGroupsUnions toEntity() {
        return RoutinesGroupsUnions.builder()
            .title(title)
            .member(member)
            .build();
    }
}