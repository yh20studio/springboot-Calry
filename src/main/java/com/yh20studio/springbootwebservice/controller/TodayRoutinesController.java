package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.todayRoutines.TodayRoutinesUpdateRequestDto;
import com.yh20studio.springbootwebservice.service.TodayRoutinesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todayRoutines")
@AllArgsConstructor
public class TodayRoutinesController {

    private TodayRoutinesService todayRoutinesService;

    // Post Method
    @PostMapping(value="", produces = "application/json; charset=UTF-8")
    public TodayRoutinesMainResponseDto saveTodayRoutines(@RequestBody TodayRoutinesSaveRequestDto dto){
        return todayRoutinesService.save(dto);
    }

    // List로 여러 데이터가 들어올 경우 Post Method
    @PostMapping(value="/list", produces = "application/json; charset=UTF-8")
    public List<TodayRoutinesMainResponseDto> saveTodayRoutinesList(@RequestBody List<TodayRoutinesSaveRequestDto> dto){
        return todayRoutinesService.saveList(dto);
    }

    // Put Method
    @PutMapping(value="/{id}", produces = "application/json; charset=UTF-8")
    public TodayRoutinesMainResponseDto updateTodayRoutines(@PathVariable("id") Long id, @RequestBody TodayRoutinesUpdateRequestDto dto){
        return todayRoutinesService.update(id, dto);
    }

    // Delete Method
    @DeleteMapping(value="/{id}", produces = "application/json; charset=UTF-8")
    public String deleteTodayRoutines(@PathVariable("id") Long id){
        todayRoutinesService.delete(id);
        return "delete";
    }

}
