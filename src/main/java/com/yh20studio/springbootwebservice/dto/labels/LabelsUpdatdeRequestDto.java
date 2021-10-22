package com.yh20studio.springbootwebservice.dto.labels;

import com.yh20studio.springbootwebservice.domain.labelColors.LabelColors;
import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LabelsUpdatdeRequestDto {

    private Long id;
    private String title;
    private Integer sequence;
    private LabelColors labelColors;
    private Member member;

    @Builder
    public LabelsUpdatdeRequestDto(Long id, String title, Integer sequence, LabelColors labelColors, Member member){
        this.id = id;
        this.title = title;
        this.sequence = sequence;
        this.labelColors = labelColors;
        this.member = member;
    }

    public Labels toEntity(){
        return Labels.builder()
                .title(title)
                .sequence(sequence)
                .label_colors(labelColors)
                .member(member)
                .build();
    }



}