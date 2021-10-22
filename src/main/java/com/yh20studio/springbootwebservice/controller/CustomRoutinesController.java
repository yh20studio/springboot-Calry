package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.customRoutines.CustomRoutinesMainResponseDto;
import com.yh20studio.springbootwebservice.dto.customRoutines.CustomRoutinesSaveRequestDto;
import com.yh20studio.springbootwebservice.service.CustomRoutinesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomRoutinesController {

    private CustomRoutinesService customRoutinesService;

    // Get Method
    @GetMapping(value="/customRoutines", produces = "application/json; charset=UTF-8")
    public List<CustomRoutinesMainResponseDto> findMyAllDescCustomRoutines(){
        return customRoutinesService.getMyAllDesc();
    }

    // Post Method
    @PostMapping(value="/customRoutines", produces = "application/json; charset=UTF-8")
    public CustomRoutinesMainResponseDto saveCustomRoutines(@RequestBody CustomRoutinesSaveRequestDto dto){
        return customRoutinesService.save(dto);
    }

    // Put Method
    @PutMapping(value="/customRoutines/{id}", produces = "application/json; charset=UTF-8")
    public CustomRoutinesMainResponseDto updateCustomRoutines(@PathVariable("id") Long id, @RequestBody CustomRoutinesSaveRequestDto dto){
        return customRoutinesService.update(id, dto);
    }

    // Delete Method
    @DeleteMapping(value="/customRoutines/{id}", produces = "application/json; charset=UTF-8")
    public String deleteCustomRoutines(@PathVariable("id") Long id){
        customRoutinesService.delete(id);
        return "delete";
    }

}
