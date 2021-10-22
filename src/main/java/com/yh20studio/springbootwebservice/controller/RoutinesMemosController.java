package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.routinesMemos.MemosMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesMemos.MemosSaveRequestDto;
import com.yh20studio.springbootwebservice.service.RoutinesMemosService;
import org.springframework.web.bind.annotation.*;

public class RoutinesMemosController {

    private RoutinesMemosService routinesMemosService;

    // Post Method
    @PostMapping(value="/routines/memos", produces = "application/json; charset=UTF-8")
    public MemosMainResponseDto saveRoutinesMemos(@RequestBody MemosSaveRequestDto dto){
        return routinesMemosService.save(dto);
    }

    // Put Method
    @PutMapping(value="/routines/memos/{id}", produces = "application/json; charset=UTF-8")
    public MemosMainResponseDto updateRoutinesMemos(@PathVariable("id") Long id, @RequestBody MemosSaveRequestDto dto){
        return routinesMemosService.update(id, dto);
    }

    // Delete Method
    @DeleteMapping(value="/routines/memos/{id}", produces = "application/json; charset=UTF-8")
    public String deleteRoutinesMemos(@PathVariable("id") Long id){
        routinesMemosService.delete(id);
        return "delete";
    }

}
