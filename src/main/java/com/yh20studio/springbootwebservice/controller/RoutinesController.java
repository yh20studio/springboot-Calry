package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.routines.RoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routines.RoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.service.RoutinesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RoutinesController {

    private RoutinesService routinesService;

    // Get Method
    @GetMapping(value="/routines", produces = "application/json; charset=UTF-8")
    public List<RoutinesMainResponseDto> findMyAllDescRoutines(){
        return routinesService.getMyAllDesc();
    }

    // Post Method
    @PostMapping(value="/routines", produces = "application/json; charset=UTF-8")
    public RoutinesMainResponseDto saveRoutines(@RequestBody RoutinesSaveRequestDto dto){
        return routinesService.save(dto);
    }

    // Put Method
    @PutMapping(value="/routines/{id}", produces = "application/json; charset=UTF-8")
    public RoutinesMainResponseDto updateRoutines(@PathVariable("id") Long id, @RequestBody RoutinesSaveRequestDto dto){
        return routinesService.update(id, dto);
    }

    // Delete Method
    @DeleteMapping(value="/routines/{id}", produces = "application/json; charset=UTF-8")
    public String deleteRoutines(@PathVariable("id") Long id){
        routinesService.delete(id);
        return "delete";
    }

}
