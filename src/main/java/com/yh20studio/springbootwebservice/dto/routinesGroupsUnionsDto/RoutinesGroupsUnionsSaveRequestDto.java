package com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto;

import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.domain.routinesGroups.RoutinesGroups;
import com.yh20studio.springbootwebservice.domain.routinesGroupsUnions.RoutinesGroupsUnions;
import com.yh20studio.springbootwebservice.dto.routinesGroups.RoutinesGroupsSaveRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
public class RoutinesGroupsUnionsSaveRequestDto {

    private String title;
    private List<RoutinesGroupsSaveRequestDto> routinesGroupsList = new ArrayList<>();
    private Member member;

    @Builder
    public RoutinesGroupsUnionsSaveRequestDto(String title,
        List<RoutinesGroupsSaveRequestDto> routinesGroupsList) {
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