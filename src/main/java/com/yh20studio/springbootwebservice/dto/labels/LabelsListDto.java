package com.yh20studio.springbootwebservice.dto.labels;

import com.yh20studio.springbootwebservice.domain.labels.Labels;
import lombok.Getter;

import java.util.List;

@Getter
public class LabelsListDto {

    private List<LabelsUpdatdeRequestDto> labels_list;
}
