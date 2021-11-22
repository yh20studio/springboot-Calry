package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.routinesMemos.RoutinesMemosMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesMemos.RoutinesMemosSaveRequestDto;
import com.yh20studio.springbootwebservice.service.RoutinesMemosService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/routines/memos")
@AllArgsConstructor
public class RoutinesMemosController {

    private RoutinesMemosService routinesMemosService;

    // Post Method
    @PostMapping(value = "", produces = "application/json; charset=UTF-8")
    public RoutinesMemosMainResponseDto saveRoutinesMemos(
        @RequestBody RoutinesMemosSaveRequestDto dto) {
        return routinesMemosService.save(dto);
    }

    // Put Method
    @PutMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public RoutinesMemosMainResponseDto updateRoutinesMemos(@PathVariable("id") Long id,
        @RequestBody RoutinesMemosSaveRequestDto dto) {
        return routinesMemosService.update(id, dto);
    }

    // Delete Method
    @DeleteMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public String deleteRoutinesMemos(@PathVariable("id") Long id) {
        routinesMemosService.delete(id);
        return "delete";
    }
}
