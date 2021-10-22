package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.quickSchedules.QuickSchedulesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.quickSchedules.QuickSchedulesSaveRequestDto;
import com.yh20studio.springbootwebservice.service.QuickSchedulesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class QuickSchedulesController {

    private QuickSchedulesService quickSchedulesService;

    // Get Method
    @GetMapping(value="/quickSchedules", produces = "application/json; charset=UTF-8")
    public List<QuickSchedulesMainResponseDto> getQuickSchedules(){
        return quickSchedulesService.getQuickSchedules();
    }

    // Post Method
    @PostMapping(value="/quickSchedules", produces = "application/json; charset=UTF-8")
    public QuickSchedulesMainResponseDto saveQuickSchedules(@RequestBody QuickSchedulesSaveRequestDto dto){
        return quickSchedulesService.save(dto);
    }

    // Put Method
    @PutMapping(value="/quickSchedules/{id}", produces = "application/json; charset=UTF-8")
    public QuickSchedulesMainResponseDto updateQuickSchedules(@PathVariable("id") Long id, @RequestBody QuickSchedulesSaveRequestDto quickSchedulesSaveRequestDto){
        return quickSchedulesService.update(id, quickSchedulesSaveRequestDto);
    }

    // Delete Method
    @DeleteMapping(value="/quickSchedules/{id}", produces = "application/json; charset=UTF-8")
    public String deleteQuickSchedules(@PathVariable("id") Long id){
        quickSchedulesService.delete(id);
        return "delete";
    }
}
