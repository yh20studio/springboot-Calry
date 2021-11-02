package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.quickSchedules.QuickSchedulesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.quickSchedules.QuickSchedulesSaveRequestDto;
import com.yh20studio.springbootwebservice.service.QuickSchedulesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/quickSchedules")
public class QuickSchedulesController {

    private QuickSchedulesService quickSchedulesService;

    // Get Method
    @GetMapping(value="", produces = "application/json; charset=UTF-8")
    public List<QuickSchedulesMainResponseDto> getQuickSchedules(){
        return quickSchedulesService.getQuickSchedules();
    }

    // Post Method
    @PostMapping(value="", produces = "application/json; charset=UTF-8")
    public QuickSchedulesMainResponseDto saveQuickSchedules(@RequestBody QuickSchedulesSaveRequestDto dto){
        return quickSchedulesService.save(dto);
    }

    // Put Method
    @PutMapping(value="/{id}", produces = "application/json; charset=UTF-8")
    public QuickSchedulesMainResponseDto updateQuickSchedules(@PathVariable("id") Long id, @RequestBody QuickSchedulesSaveRequestDto quickSchedulesSaveRequestDto){
        return quickSchedulesService.update(id, quickSchedulesSaveRequestDto);
    }

    // Delete Method
    @DeleteMapping(value="/{id}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> deleteQuickSchedules(@PathVariable("id") Long id){
        quickSchedulesService.delete(id);
        return ResponseEntity.ok("delete");
    }
}
