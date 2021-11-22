package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto.RoutinesGroupsUnionsMainResponseDto;
import com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto.RoutinesGroupsUnionsSaveRequestDto;
import com.yh20studio.springbootwebservice.dto.routinesGroupsUnionsDto.RoutinesGroupsUnionsUpdateRequestDto;
import com.yh20studio.springbootwebservice.service.RoutinesGroupsUnionsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routinesGroups/unions")
@AllArgsConstructor
public class RoutinesGroupsUnionsController {

    private RoutinesGroupsUnionsService routinesGroupsUnionsService;

    // Get Method
    @GetMapping(value = "", produces = "application/json; charset=UTF-8")
    public List<RoutinesGroupsUnionsMainResponseDto> getMyRoutinesGroupsUnions() {
        return routinesGroupsUnionsService.getMyRoutinesGroupsUnions();
    }

    // Post Method
    @PostMapping(value = "", produces = "application/json; charset=UTF-8")
    public RoutinesGroupsUnionsMainResponseDto saveRoutinesGroupsUnions(
        @RequestBody RoutinesGroupsUnionsSaveRequestDto dto) {
        return routinesGroupsUnionsService.saveRoutinesGroupsUnions(dto);
    }

    // Put Method
    @PutMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public RoutinesGroupsUnionsMainResponseDto updateRoutinesGroupsUnions(
        @PathVariable("id") Long id, @RequestBody RoutinesGroupsUnionsUpdateRequestDto dto) {
        return routinesGroupsUnionsService.updateRoutinesGroupsUnions(id, dto);
    }

    // Delete Method
    @DeleteMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public String deleteRoutinesGroupsUnions(@PathVariable("id") Long id) {
        routinesGroupsUnionsService.deleteRoutinesGroupsUnions(id);
        return "delete";
    }
}
