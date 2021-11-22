package com.yh20studio.springbootwebservice.dto.labels;

import com.yh20studio.springbootwebservice.domain.labelColors.LabelColors;
import com.yh20studio.springbootwebservice.domain.labels.Labels;
import com.yh20studio.springbootwebservice.domain.member.Member;
import com.yh20studio.springbootwebservice.dto.labelColors.LabelColorsMainResponseDto;
import lombok.Getter;

@Getter
public class LabelsMainResponseDto {

    private Long id;
    private String title;
    private Integer sequence;
    private LabelColorsMainResponseDto label_colors;

    public LabelsMainResponseDto(Labels entity) {
        id = entity.getId();
        title = entity.getTitle();
        label_colors = new LabelColorsMainResponseDto(entity.getLabel_colors());
        sequence = entity.getSequence();
    }
}