package com.yh20studio.springbootwebservice.dto.labelColors;

import com.yh20studio.springbootwebservice.domain.labelColors.LabelColors;
import lombok.Getter;

@Getter
public class LabelColorsMainResponseDto {

    private Long id;
    private String title;
    private String code;

    public LabelColorsMainResponseDto(LabelColors entity){
        id = entity.getId();
        title = entity.getTitle();
        code = entity.getCode();
    }

}