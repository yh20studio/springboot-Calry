package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.labels.LabelsListDto;
import com.yh20studio.springbootwebservice.dto.labels.LabelsMainResponseDto;
import com.yh20studio.springbootwebservice.service.LabelsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class LabelsController {

    private LabelsService labelsService;

    // Get Method
    @GetMapping(value="/labels", produces = "application/json; charset=UTF-8")
    public List<LabelsMainResponseDto> getLabels(){
        return labelsService.getMyAllDesc();
    }

    // Put Method, Labels의 order를 바꾸기 위한 Method
    @PutMapping(value="/labels", produces = "application/json; charset=UTF-8")
    public List<LabelsMainResponseDto> updateLabels(@RequestBody LabelsListDto labelsListDto){
        return labelsService.update(labelsListDto);
    }

}
